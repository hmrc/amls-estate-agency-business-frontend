/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers.actions

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.routes
import models.requests.IdentifierRequest
import play.api.{Logger, Logging}
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.~
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.http.HeaderCarrierConverter
import utils.UrlHelper

import scala.concurrent.{ExecutionContext, Future}

@com.google.inject.ImplementedBy(classOf[AuthenticatedIdentifierAction])
trait IdentifierAction extends ActionBuilder[IdentifierRequest, AnyContent]

final case class enrolmentNotFound(msg: String = "enrolmentNotFound") extends AuthorisationException(msg)

class AuthenticatedIdentifierAction @Inject()(
                                               override val authConnector: AuthConnector,
                                               config: FrontendAppConfig,
                                               val parser: BodyParsers.Default
                                             )
                                             (implicit val executionContext: ExecutionContext)
  extends IdentifierAction with AuthorisedFunctions with ActionRefiner[Request, IdentifierRequest] with Logging {

  private val amlsKey       = "HMRC-MLR-ORG"
  private val amlsNumberKey = "MLRRefNumber"
  private val saKey         = "IR-SA"
  private val ctKey         = "IR-CT"

  def unauthorisedUrl= routes.UnauthorisedController.onPageLoad.url

  // $COVERAGE-OFF$
  def exceptionLogger(aex: AuthorisationException) = {
    logger.debug(s"AuthenticatedIdentifierAction:Refine - ${aex.getClass}:", aex)
  }
  def enrolmentMessage(message: String, parameters: Option[Enrolments]) = {
    logger.debug(message + parameters.getOrElse(""))
  }
  // $COVERAGE-ON$

  override protected def refine[A](request: Request[A]): Future[Either[Result, IdentifierRequest[A]]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromRequestAndSession(request, request.session)

    authorised(authPredicate).retrieve(
      Retrievals.allEnrolments and
        Retrievals.credentials and
        Retrievals.affinityGroup
    ) {
      case enrolments ~ Some(credentials) ~ Some(affinityGroup) =>
        logger.debug("DefaultAuthAction:Refine - Enrolments:" + enrolments)

        Future.successful(
          Right(
            IdentifierRequest(
              request,
              amlsRefNo(enrolments),
              credentials.providerId,
              accountTypeAndId(affinityGroup, enrolments, credentials.providerId),
              affinityGroup
            )
          )
        )
      case _ =>
        logger.debug("DefaultAuthAction:Refine - Non match (enrolments ~ Some(credentials) ~ Some(affinityGroup))")
        Future.successful(Left(Redirect(Call("GET", unauthorisedUrl))))
    }.recover[Either[Result, IdentifierRequest[A]]] {
      case nas: NoActiveSession =>
        exceptionLogger(nas)
        Left(Redirect(Call("GET", config.loginUrl)))
      case ie: InsufficientEnrolments =>
        exceptionLogger(ie)
        Left(Redirect(Call("GET", unauthorisedUrl)))
      case icl: InsufficientConfidenceLevel =>
        exceptionLogger(icl)
        Left(Redirect(Call("GET", unauthorisedUrl)))
      case uap: UnsupportedAuthProvider =>
        exceptionLogger(uap)
        Left(Redirect(Call("GET", unauthorisedUrl)))
      case uag: UnsupportedAffinityGroup =>
        exceptionLogger(uag)
        Left(Redirect(Call("GET", unauthorisedUrl)))
      case ucr: UnsupportedCredentialRole =>
        exceptionLogger(ucr)
        Left(Redirect(Call("GET", unauthorisedUrl)))
      case enf: enrolmentNotFound =>
        exceptionLogger(enf)
        Left(Redirect(Call("GET", unauthorisedUrl)))
      case e : AuthorisationException =>
        exceptionLogger(e)
        Left(Redirect(Call("GET", unauthorisedUrl)))
    }
  }

  private def authPredicate = {
    User and (AffinityGroup.Organisation or (Enrolment(saKey) or Enrolment(ctKey)))
  }

  private def amlsRefNo(enrolments: Enrolments): Option[String] = {
    val amlsRefNumber = for {
      enrolment      <- enrolments.getEnrolment(amlsKey)
      amlsIdentifier <- enrolment.getIdentifier(amlsNumberKey)
    } yield amlsIdentifier.value
    amlsRefNumber
  }

  private def getActiveEnrolment(enrolments: Enrolments, key: String) = {
    /*
    *  Look for activated enrolments only for SA and CT.
    *  Enrolments can be 'Activated' or 'NotYetActivated'.
    */
    enrolments.getEnrolment(key).filter(e => e.isActivated)
  }

  private def accountTypeAndId(affinityGroup: AffinityGroup,
                               enrolments: Enrolments,
                               credId: String) = {
    /*
    * Set the `accountType` to `"org"` if `affinityGroup = "Organisation"` (which you get through retrievals)
    * Set the `accountId` as a hash of the CredId. Its possible to get the `credId` through retrievals
    */

    /*
     * For an affinity group other than Org;
     * Retrieve the enrolments through retrievals.
     * If one of them is `"IR-SA"`, you can set `accountType` to `"sa"` and `accountId` to the `value` for `key` `"UTR"`
     * If one of them is `"IR-CT"`, you can set `accountType` to `"ct"` and `accountId` to the `value` for `key` `"UTR"`

     */

    affinityGroup match {
      case AffinityGroup.Organisation => ("org", UrlHelper.hash(credId))
      case _ =>

        val sa = for {
          enrolment <- getActiveEnrolment(enrolments, saKey)
          utr       <- enrolment.getIdentifier("UTR")
        } yield "sa" -> utr.value

        val ct = for {
          enrolment <- getActiveEnrolment(enrolments, ctKey)
          utr       <- enrolment.getIdentifier("UTR")
        } yield "ct" -> utr.value

        (sa orElse ct).getOrElse(throw new enrolmentNotFound)
    }
  }
}