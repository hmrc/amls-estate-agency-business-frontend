/*
 * Copyright 2023 HM Revenue & Customs
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

package utils

import base.SpecBase
import connectors.{AMLSBackEndConnector, AMLSConnector}
import models.{DateOfChangeResponse, ReadStatusResponse, UserAnswers}
import org.mockito.MockitoSugar
import play.api.inject.bind
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class ControllerHelperSpec extends SpecBase with MockitoSugar {

  implicit val hc              = HeaderCarrier()
  val mockAMLSConnector        = mock[AMLSConnector]
  val mockAMLSBackEndConnector = mock[AMLSBackEndConnector]
  val amlsRefNo                = "refNo"
  val accountTypeId            = ("foo", "bar")
  val statusResponse           = mock[ReadStatusResponse]
  val mockDateOfChangeResponse = mock[DateOfChangeResponse]
  val credId                   = "foo"
  val mockAnswers              = UserAnswers()

  val application =
    applicationBuilder(userAnswers = Some(emptyUserAnswers))
      .overrides(
        bind[AMLSConnector].toInstance(mockAMLSConnector),
        bind[AMLSBackEndConnector].toInstance(mockAMLSBackEndConnector)
      )
      .build()

  val helper = application.injector.instanceOf[ControllerHelper]

  "getApplicationStatus" should {

    "return the application status as NotYetSubmitted where no amls reference number" in {
      val response = helper.getApplicationStatus(None, accountTypeId)

      response.map(r => r mustBe "NotYetSubmitted")
    }

    "return an application status of Approved where amls reference number present" in {

      when(statusResponse.formBundleStatus).thenReturn("Approved")

      when(mockAMLSBackEndConnector.status(amlsRefNo, accountTypeId)).thenReturn(Future.successful(statusResponse))

      val response = helper.getApplicationStatus(None, accountTypeId)

      response.map(r => r mustBe "Approved")
    }
  }

  "requireDateOfChange" should {

    "return false where date of change not required" in {

      when(mockDateOfChangeResponse.requireDateOfChange).thenReturn(false)

      when(mockAMLSConnector.requireDateOfChange(credId, "Approved", mockAnswers)).thenReturn(
        Future.successful(mockDateOfChangeResponse)
      )

      val response = helper.requireDateOfChange(credId, mockAnswers, "Approved")

      response.map(r => r mustBe false)
    }

    "return true where date of change is required" in {

      when(mockDateOfChangeResponse.requireDateOfChange).thenReturn(true)

      when(mockAMLSConnector.requireDateOfChange(credId, "Approved", mockAnswers)).thenReturn(
        Future.successful(mockDateOfChangeResponse)
      )

      val response = helper.requireDateOfChange(credId, mockAnswers, "Approved")

      response.map(r => r mustBe true)
    }
  }
}