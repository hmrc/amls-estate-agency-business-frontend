/*
 * Copyright 2024 HM Revenue & Customs
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

import base.SpecBase
import com.google.inject.Inject
import controllers.actions.IdentifierActionSpec.{agentCtAuthRetrievals, agentSaAuthRetrievals, emptyAuthRetrievals, fakeAuthConnector, orgAuthRetrievals}
import controllers.routes
import play.api.mvc.{BodyParsers, Results}
import play.api.test.Helpers._
import uk.gov.hmrc.auth.core._
import uk.gov.hmrc.auth.core.authorise.Predicate
import uk.gov.hmrc.auth.core.retrieve.{Credentials, Retrieval, ~}
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{ExecutionContext, Future}

class IdentifierActionSpec extends SpecBase {

  class Harness(authAction: IdentifierAction) {
    def onPageLoad() = authAction { _ => Results.Ok }
  }

  "Auth Action" when {

    "the user hasn't logged in" must {

      "redirect the user to log in " in {

        val application = applicationBuilder(userAnswers = None).build()

        val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

        val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new MissingBearerToken), frontendAppConfig, bodyParsers)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)

        status(result) mustBe SEE_OTHER

        redirectLocation(result).get must startWith(frontendAppConfig.loginUrl)
      }
    }

    "the user's session has expired" must {

      "redirect the user to log in " in {

        val application = applicationBuilder(userAnswers = None).build()

        val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

        val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new BearerTokenExpired), frontendAppConfig, bodyParsers)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)

        status(result) mustBe SEE_OTHER

        redirectLocation(result).get must startWith(frontendAppConfig.loginUrl)
      }
    }

    "the user doesn't have sufficient enrolments" must {

      "redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

        val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new InsufficientEnrolments), frontendAppConfig, bodyParsers)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)

        status(result) mustBe SEE_OTHER

        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad.url)
      }
    }

    "the user doesn't have sufficient confidence level" must {

      "redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

        val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new InsufficientConfidenceLevel), frontendAppConfig, bodyParsers)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)

        status(result) mustBe SEE_OTHER

        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad.url)
      }
    }

    "the user used an unaccepted auth provider" must {

      "redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

        val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new UnsupportedAuthProvider), frontendAppConfig, bodyParsers)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)

        status(result) mustBe SEE_OTHER

        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad.url)
      }
    }

    "the user has an unsupported affinity group" must {

      "redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

        val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new UnsupportedAffinityGroup), frontendAppConfig, bodyParsers)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)

        status(result) mustBe SEE_OTHER

        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad.url)
      }
    }

    "the user has an unsupported credential role" must {

      "redirect the user to the unauthorised page" in {

        val application = applicationBuilder(userAnswers = None).build()

        val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

        val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new UnsupportedCredentialRole), frontendAppConfig, bodyParsers)
        val controller = new Harness(authAction)
        val result = controller.onPageLoad()(fakeRequest)

        status(result) mustBe SEE_OTHER

        redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad.url)
      }
    }

    "AffinityGroup is Organisation" when {
      "the user has valid credentials" must {
        "return successful result" in {
          val application = applicationBuilder(userAnswers = None).build()

          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

          val authAction = new AuthenticatedIdentifierAction(fakeAuthConnector(orgAuthRetrievals), frontendAppConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(fakeRequest)

          status(result) mustBe OK
        }
      }
    }

    "AffinityGroup is not Organisation" when {
      "the user has valid credentials for sa" must {
        "return successful result" in {
          val application = applicationBuilder(userAnswers = None).build()

          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

          val authAction = new AuthenticatedIdentifierAction(fakeAuthConnector(agentSaAuthRetrievals), frontendAppConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(fakeRequest)

          status(result) mustBe OK
        }
      }

      "the user has valid credentials for ct" must {
        "return successful result" in {
          val application = applicationBuilder(userAnswers = None).build()

          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

          val authAction = new AuthenticatedIdentifierAction(fakeAuthConnector(agentCtAuthRetrievals), frontendAppConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(fakeRequest)

          status(result) mustBe OK
        }
      }

      "there is no match in retrievals" must {
        "redirect to unauthorised page" in {
          val application = applicationBuilder(userAnswers = None).build()

          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

          val authAction = new AuthenticatedIdentifierAction(fakeAuthConnector(emptyAuthRetrievals), frontendAppConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(fakeRequest)

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad.url)
        }
      }

      "there is no enrolments" must {
        "redirect to unauthorised page" in {
          val application = applicationBuilder(userAnswers = None).build()

          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

          val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new enrolmentNotFound), frontendAppConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(fakeRequest)

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad.url)
        }
      }

      "any other exception thrown" must {
        "redirect to unauthorised page" in {

          final case class OtherException(msg: String = "otherException") extends AuthorisationException(msg)

          val application = applicationBuilder(userAnswers = None).build()

          val bodyParsers = application.injector.instanceOf[BodyParsers.Default]

          val authAction = new AuthenticatedIdentifierAction(new FakeFailingAuthConnector(new OtherException()), frontendAppConfig, bodyParsers)
          val controller = new Harness(authAction)
          val result = controller.onPageLoad()(fakeRequest)

          status(result) mustBe SEE_OTHER

          redirectLocation(result) mustBe Some(routes.UnauthorisedController.onPageLoad.url)
        }
      }
    }
  }
}

class FakeFailingAuthConnector @Inject()(exceptionToReturn: Throwable) extends AuthConnector {
  val serviceUrl: String = ""

  override def authorise[A](predicate: Predicate, retrieval: Retrieval[A])(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[A] =
    Future.failed(exceptionToReturn)
}

object IdentifierActionSpec {
  private def fakeAuthConnector(stubbedRetrievalResult: Future[_]) = new AuthConnector {

    def authorise[A](predicate: Predicate, retrieval: Retrieval[A])
                    (implicit hc: HeaderCarrier, ec: ExecutionContext): Future[A] = {
      stubbedRetrievalResult.asInstanceOf[Future[A]]
    }
  }

  val amlsRegistrationNumber = "XAML0000123456789"

  val enrolments = Enrolments(Set(
    Enrolment("HMCE-VATVAR-ORG", Seq(EnrolmentIdentifier("VATRegNo", "000000000")), "Activated"),
    Enrolment("HMRC-MLR-ORG", Seq(EnrolmentIdentifier("MLRRefNumber", amlsRegistrationNumber)), "Activated")
  ))
  private def orgAuthRetrievals = Future.successful(
    new ~ (new ~(enrolments, Some(Credentials("gg", "cred-1234"))), Some(AffinityGroup.Organisation))
  )

  val enrolmentsSa = Enrolments(Set(
    Enrolment("HMCE-VATVAR-ORG", Seq(EnrolmentIdentifier("VATRegNo", "000000000")), "Activated"),
    Enrolment("HMRC-MLR-ORG", Seq(EnrolmentIdentifier("MLRRefNumber", amlsRegistrationNumber)), "Activated"),
    Enrolment("IR-SA", Seq(EnrolmentIdentifier("UTR", "saRef")), "Activated")
  ))
  private def agentSaAuthRetrievals = Future.successful(
    new ~ (new~(enrolmentsSa, Some(Credentials("gg", "cred-1234"))), Some(AffinityGroup.Agent))
  )

  val enrolmentsCt = Enrolments(Set(
    Enrolment("HMCE-VATVAR-ORG", Seq(EnrolmentIdentifier("VATRegNo", "000000000")), "Activated"),
    Enrolment("HMRC-MLR-ORG", Seq(EnrolmentIdentifier("MLRRefNumber", amlsRegistrationNumber)), "Activated"),
    Enrolment("IR-CT", Seq(EnrolmentIdentifier("UTR", "ctRef")), "Activated")
  ))
  private def agentCtAuthRetrievals = Future.successful(
    new ~ (new ~(enrolmentsCt, Some(Credentials("gg", "cred-1234"))), Some(AffinityGroup.Agent))
  )

  private def emptyAuthRetrievals = Future.successful(
    new ~(new ~(Enrolments(Set()), None), None)
  )

}
