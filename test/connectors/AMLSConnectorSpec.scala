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
import models.{DateOfChangeResponse, ReadStatusResponse, UserAnswers}
import org.mockito.ArgumentMatchers
import org.mockito.ArgumentMatchers.any
import org.mockito.Mockito.{verify, when}
import org.scalatestplus.mockito.MockitoSugar
import pages.EabServicesProvidedPage
import play.api.Configuration
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.http.client.{HttpClientV2, RequestBuilder}

import java.time.{LocalDateTime, ZoneOffset}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AMLSConnectorSpec extends SpecBase with MockitoSugar {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  val dateVal = LocalDateTime.now
  val answers = UserAnswers().set(EabServicesProvidedPage, Seq(Auctioneering)).success.value

  val mockHttpClient: HttpClientV2   = mock[HttpClientV2]
  val requestBuilder: RequestBuilder = mock[RequestBuilder]
  val amlsConnector                  = new AMLSConnector(config = app.injector.instanceOf[Configuration], mockHttpClient)

  val completeData = Json.obj(
    "eabServicesProvided"       -> Seq("businessTransfer"),
    "redressScheme"             -> "other",
    "redressSchemeDetail"       -> "other redress scheme",
    "penalisedEstateAgentsAct"  -> false,
    "penalisedProfessionalBody" -> false
  )

  val completeJson = Json.obj(
    "_id"         -> "someid",
    "data"        -> completeData,
    "lastUpdated" -> Json.obj("$date" -> dateVal.atZone(ZoneOffset.UTC).toInstant.toEpochMilli)
  )

  "GET" must {
    "successfully fetch cache" in {

      when(mockHttpClient.get(any())(any())).thenReturn(requestBuilder)
      when(requestBuilder.execute[Option[JsObject]](any(), any())).thenReturn(Future.successful(Some(completeJson)))

      amlsConnector.get("someid").futureValue mustBe Some(completeJson)
    }
  }

  "POST" must {
    "successfully write cache" in {
      val response = HttpResponse(200)

      when(requestBuilder.withBody(any())(any(), any(), any())).thenReturn(requestBuilder)
      when(requestBuilder.execute[HttpResponse](any(), any())).thenReturn(Future.successful(response))
      when(mockHttpClient.put(any())(any())).thenReturn(requestBuilder)

      amlsConnector.set("someid", answers).futureValue mustBe response

    }
  }

  "requireDateOfChange" must {
    val credId                   = "someid"
    val submissionStatus         = "Approved"
    val mockDateOfChangeResponse = mock[DateOfChangeResponse]

    "make a request to amls frontend to find out if date of change is required" in {

      when(requestBuilder.withBody(any())(any(), any(), any())).thenReturn(requestBuilder)
      when(requestBuilder.execute[DateOfChangeResponse](any(), any()))
        .thenReturn(Future.successful(mockDateOfChangeResponse))
      when(mockHttpClient.post(any())(any())).thenReturn(requestBuilder)

      amlsConnector.requireDateOfChange(credId, submissionStatus, answers).futureValue mustBe mockDateOfChangeResponse
    }
  }
}
