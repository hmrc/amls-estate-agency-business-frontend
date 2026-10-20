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

package repositories
import org.scalatest.matchers.must.Matchers._

import base.SpecBase
import ch.qos.logback.classic.Level
import connectors.AMLSConnector
import models.UserAnswers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.when
import org.scalatestplus.mockito.MockitoSugar
import play.api.libs.json.{JsObject, Json}
import play.api.{Configuration, Logger}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.tools.LogCapturing

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AMLSFrontEndSessionRepositorySpec extends SpecBase with MockitoSugar with LogCapturing {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  val mockAmlsConnector: AMLSConnector = mock[AMLSConnector]
  val repository                       =
    new DefaultAMLSFrontEndSessionRepository(mockAmlsConnector, app.injector.instanceOf[Configuration])
  val repositoryLogger: Logger         = Logger(classOf[DefaultAMLSFrontEndSessionRepository])

  "get" must {
    "return the saved answers and log no error when the fetch succeeds" in {
      when(mockAmlsConnector.get(any())(any()))
        .thenReturn(Future.successful(Some(Json.toJson(UserAnswers()).as[JsObject])))

      withCaptureOfLoggingFrom(repositoryLogger) { logs =>
        repository.get("someid").futureValue mustBe Some(UserAnswers())
        logs.exists(_.getLevel == Level.ERROR) mustBe false
      }
    }

    "return None and log an error with the exception attached when the fetch fails" in {
      when(mockAmlsConnector.get(any())(any())).thenReturn(Future.failed(new RuntimeException("backend down")))

      withCaptureOfLoggingFrom(repositoryLogger) { logs =>
        repository.get("someid").futureValue mustBe None

        val event = logs.find(
          _.getFormattedMessage.contains(
            "[DefaultAMLSFrontEndSessionRepository][get] - Failed to fetch or parse session data"
          )
        )
        event.map(_.getLevel) mustBe Some(Level.ERROR)
        Option(event.get.getThrowableProxy).map(_.getMessage) mustBe Some("backend down")
      }
    }

    "return None and log an error when the saved data cannot be parsed" in {
      when(mockAmlsConnector.get(any())(any())).thenReturn(Future.successful(Some(Json.obj("data" -> "not-an-object"))))

      withCaptureOfLoggingFrom(repositoryLogger) { logs =>
        repository.get("someid").futureValue mustBe None
        logs.exists(e =>
          e.getLevel == Level.ERROR &&
            e.getFormattedMessage.contains(
              "[DefaultAMLSFrontEndSessionRepository][get] - Failed to fetch or parse session data"
            )
        ) mustBe true
      }
    }
  }

  "set" must {
    "return true and log no error when the save succeeds" in {
      when(mockAmlsConnector.set(any(), any())(any())).thenReturn(Future.successful(HttpResponse(200, "ok")))

      withCaptureOfLoggingFrom(repositoryLogger) { logs =>
        repository.set("someid", UserAnswers()).futureValue mustBe true
        logs.exists(_.getLevel == Level.ERROR) mustBe false
      }
    }

    "return false and log an error with the exception attached when the save fails" in {
      when(mockAmlsConnector.set(any(), any())(any())).thenReturn(Future.failed(new RuntimeException("backend down")))

      withCaptureOfLoggingFrom(repositoryLogger) { logs =>
        repository.set("someid", UserAnswers()).futureValue mustBe false

        val event =
          logs.find(
            _.getFormattedMessage.contains("[DefaultAMLSFrontEndSessionRepository][set] - Failed to save session data")
          )
        event.map(_.getLevel) mustBe Some(Level.ERROR)
        Option(event.get.getThrowableProxy).map(_.getMessage) mustBe Some("backend down")
      }
    }
  }
}
