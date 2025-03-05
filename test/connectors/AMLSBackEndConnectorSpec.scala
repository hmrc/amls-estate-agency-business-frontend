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
import models.ReadStatusResponse
import org.mockito.Mockito.when
import org.mockito.ArgumentMatchers.any
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.client.{HttpClientV2, RequestBuilder}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class AMLSBackEndConnectorSpec extends SpecBase with MockitoSugar {

  implicit val hc: HeaderCarrier = HeaderCarrier()

  val amlsRefNo      = "refNo"
  val accountTypeId  = ("foo", "bar")
  val accountType    = accountTypeId._1
  val accountId      = accountTypeId._2
  val statusResponse = mock[ReadStatusResponse]

  val mockHttpClient: HttpClientV2   = mock[HttpClientV2]
  val requestBuilder: RequestBuilder = mock[RequestBuilder]
  val amlsBackEndConnector           = new AMLSBackEndConnector(config = app.injector.instanceOf[Configuration], mockHttpClient)

  "status" must {
    "successfully return a status" in {

      when(mockHttpClient.get(any())(any())).thenReturn(requestBuilder)
      when(requestBuilder.execute[ReadStatusResponse](any(), any())).thenReturn(Future.successful(statusResponse))

      amlsBackEndConnector.status("someid", ("accounttype", "type")).futureValue mustBe statusResponse
    }
  }
}
