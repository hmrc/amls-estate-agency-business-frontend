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

package connectors

import base.SpecBase
import models.EabServicesProvided.Auctioneering
import models.{DateOfChangeResponse, UserAnswers}
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.EabServicesProvidedPage
import play.api.Configuration
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}

import java.time.{LocalDateTime, ZoneOffset}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AMLSConnectorSpec extends SpecBase with MockitoSugar {

  implicit val hc: HeaderCarrier = HeaderCarrier()
  val amlsConnector = new AMLSConnector(config = mock[Configuration], httpClient = mock[HttpClient])
  val dateVal       = LocalDateTime.now
  val answers       = UserAnswers().set(EabServicesProvidedPage,  Seq(Auctioneering)).success.value

  val completeData  = Json.obj(
    "eabServicesProvided"       -> Seq("businessTransfer"),
    "redressScheme"             -> "other",
    "redressSchemeDetail"       -> "other redress scheme",
    "penalisedEstateAgentsAct"  -> false,
    "penalisedProfessionalBody" -> false
  )

  val completeJson  = Json.obj(
    "_id"         -> "someid",
    "data"        -> completeData,
    "lastUpdated" -> Json.obj("$date" -> dateVal.atZone(ZoneOffset.UTC).toInstant.toEpochMilli)
  )

  "GET" must {
    "successfully fetch cache" in {
      val getUrl = s"${amlsConnector.url}/get/someid"

      when {
        amlsConnector.httpClient.GET[Option[JsObject]](ArgumentMatchers.eq(getUrl), any(), any())(any(), any(), any())
      } thenReturn Future.successful(Some(completeJson))

      whenReady(amlsConnector.get("someid")) {
        _ mustBe Some(completeJson)
      }
    }
  }

  "POST" must {
    "successfully write cache" in {
      val putUrl = s"${amlsConnector.url}/set/someid"

      amlsConnector.set("someid", answers)
      verify(amlsConnector.httpClient).PUT(ArgumentMatchers.eq(putUrl), ArgumentMatchers.eq(answers), any())(any(), any(), any(), any())
    }
  }

  "requireDateOfChange" must {
    val credId                   = "someid"
    val submissionStatus         = "Approved"
    val mockDateOfChangeResponse = mock[DateOfChangeResponse]

    "make a request to amls frontend to find out if date of change is required" in {
      val url = s"${amlsConnector.url}/require-date-change/$credId/$submissionStatus"

      when(
        amlsConnector.httpClient.POST[UserAnswers, DateOfChangeResponse](ArgumentMatchers.eq(url), ArgumentMatchers.eq(answers), any()
        )(any(), any(), any(), any())).thenReturn(Future.successful(mockDateOfChangeResponse))

      amlsConnector.requireDateOfChange(credId, submissionStatus, answers)

      verify(amlsConnector.httpClient).POST[UserAnswers, DateOfChangeResponse](ArgumentMatchers.eq(url), ArgumentMatchers.eq(answers), any()
      )(any(), any(), any(), any())
    }
  }
}