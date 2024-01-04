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

package controllers

import base.SpecBase
import forms.EabServicesProvidedFormProvider
import models.{EabServicesProvided, NormalMode, UserAnswers}
import navigation.{FakeNavigator, Navigator}
import org.mockito.ArgumentMatchers.any
import org.mockito.MockitoSugar
import pages.EabServicesProvidedPage
import play.api.inject.bind
import play.api.mvc.Call
import play.api.test.FakeRequest
import play.api.test.Helpers._
import repositories.AMLSFrontEndSessionRepository
import utils.ControllerHelper
import views.html.EabServicesProvidedView

import scala.concurrent.Future

class EabServicesProvidedControllerSpec extends SpecBase with MockitoSugar {

  def onwardRoute = Call("GET", "/foo")

  lazy val eabServicesProvidedRoute = routes.EabServicesProvidedController.onPageLoad(NormalMode).url

  val formProvider = new EabServicesProvidedFormProvider()
  val form = formProvider()

  "EabServicesProvided Controller" must {

    "return OK and the correct view for a GET" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request = FakeRequest(GET, eabServicesProvidedRoute)

      val result = route(application, request).value

      val view = application.injector.instanceOf[EabServicesProvidedView]

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form, NormalMode)(request, messages).toString

      application.stop()
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = UserAnswers().set(EabServicesProvidedPage, EabServicesProvided.values).success.value

      val application = applicationBuilder(userAnswers = Some(userAnswers)).build()

      val request = FakeRequest(GET, eabServicesProvidedRoute)

      val view = application.injector.instanceOf[EabServicesProvidedView]

      val result = route(application, request).value

      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(form.fill(EabServicesProvided.values), NormalMode)(request, messages).toString

      application.stop()
    }

    "redirect to the right page when valid data is submitted and date of change required" in {

      val mockSessionRepository = mock[AMLSFrontEndSessionRepository]
      val mockControllerHelper = mock[ControllerHelper]

      when(mockSessionRepository.set(any(), any())(any())) thenReturn Future.successful(true)
      when(mockControllerHelper.getApplicationStatus(any(), any())(any(), any())) thenReturn Future.successful("Approved")
      when(mockControllerHelper.requireDateOfChange(any(), any(), any())(any(), any())) thenReturn Future.successful(true)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[AMLSFrontEndSessionRepository].toInstance(mockSessionRepository),
            bind[ControllerHelper].toInstance(mockControllerHelper)
          )
          .build()

      val request =
        FakeRequest(POST, eabServicesProvidedRoute)
          .withFormUrlEncodedBody(("value[0]", EabServicesProvided.values.head.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "redirect to the right page when valid data is submitted and date of change is not required" in {

      val mockSessionRepository = mock[AMLSFrontEndSessionRepository]
      val mockControllerHelper = mock[ControllerHelper]

      when(mockSessionRepository.set(any(), any())(any())) thenReturn Future.successful(true)

      when(
        mockControllerHelper.getApplicationStatus(any(), any())(any(), any())
      ) thenReturn Future.successful("NotYetSubmitted")

      when(
        mockControllerHelper.requireDateOfChange(any(), any(), any())(any(), any())
      ) thenReturn Future.successful(false)

      val application =
        applicationBuilder(userAnswers = Some(emptyUserAnswers))
          .overrides(
            bind[Navigator].toInstance(new FakeNavigator(onwardRoute)),
            bind[AMLSFrontEndSessionRepository].toInstance(mockSessionRepository),
            bind[ControllerHelper].toInstance(mockControllerHelper)
          )
          .build()

      val request =
        FakeRequest(POST, eabServicesProvidedRoute)
          .withFormUrlEncodedBody(("value[0]", EabServicesProvided.values.head.toString))

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual onwardRoute.url

      application.stop()
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val application = applicationBuilder(userAnswers = Some(emptyUserAnswers)).build()

      val request =
        FakeRequest(POST, eabServicesProvidedRoute)
          .withFormUrlEncodedBody(("value", "invalid value"))

      val boundForm = form.bind(Map("value" -> "invalid value"))

      val view = application.injector.instanceOf[EabServicesProvidedView]

      val result = route(application, request).value

      status(result) mustEqual BAD_REQUEST

      contentAsString(result) mustEqual
        view(boundForm, NormalMode)(request, messages).toString

      application.stop()
    }
  }
}
