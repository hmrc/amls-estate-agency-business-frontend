/*
 * Copyright 2020 HM Revenue & Customs
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
import play.api.Logger
import play.api.mvc.Results._
import play.api.mvc._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.retrieve.v2.Retrievals
import uk.gov.hmrc.auth.core.retrieve.~
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.HeaderCarrierConverter

import scala.concurrent.{ExecutionContext, Future}

@com.google.inject.ImplementedBy(classOf[AuthenticatedIdentifierAction])
trait IdentifierAction extends ActionBuilder[IdentifierRequest, AnyContent]

final case class enrolmentNotFound(msg: String = "enrolmentNotFound") extends AuthorisationException(msg)

class AuthenticatedIdentifierAction @Inject()(
                                               override val authConnector: AuthConnector,
                                               config: FrontendAppConfig,
                                               val parser: BodyParsers.Default
                                             )
                                             (implicit val executionContext: ExecutionContext) extends IdentifierAction with AuthorisedFunctions with ActionRefiner[Request, IdentifierRequest] {

  private val amlsKey       = "HMRC-MLR-ORG"
  private val amlsNumberKey = "MLRRefNumber"
  private val saKey         = "IR-SA"
  private val ctKey         = "IR-CT"

  def unauthorisedUrl= routes.UnauthorisedController.onPageLoad().url

  // $COVERAGE-OFF$
  def exceptionLogger(aex: AuthorisationException) = {
    Logger.debug(s"AuthenticatedIdentifierAction:Refine - ${aex.getClass}:", aex)
  }
  def enrolmentMessage(message: String, parameters: Option[Enrolments]) = {
    Logger.debug(message + parameters.getOrElse(""))
  }
  // $COVERAGE-ON$

  override protected def refine[A](request: Request[A]): Future[Either[Result, IdentifierRequest[A]]] = {

    implicit val hc: HeaderCarrier = HeaderCarrierConverter.fromHeadersAndSession(request.headers, Some(request.session))

    authorised(authPredicate).retrieve(
      Retrievals.allEnrolments and
        Retrievals.credentials and
        Retrievals.affinityGroup
    ) {
      case enrolments ~ Some(credentials) ~ Some(affinityGroup) =>
        Logger.debug("DefaultAuthAction:Refine - Enrolments:" + enrolments)

        Future.successful(
          Right(
            IdentifierRequest(
              request,
              amlsRefNo(enrolments),
              credentials.providerId,
              affinityGroup
            )
          )
        )
      case _ =>
        Logger.debug("DefaultAuthAction:Refine - Non match (enrolments ~ Some(credentials) ~ Some(affinityGroup))")
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
}