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

package connectors

import base.SpecBase
import models.{ReadStatusResponse}
import org.mockito.Matchers.{any, eq => eqTo}
import org.mockito.Mockito._
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

class AMLSBackEndConnectorSpec extends SpecBase with MockitoSugar {

  implicit val hc          = HeaderCarrier()
  val amlsBackEndConnector = new AMLSBackEndConnector(config = mock[Configuration], httpClient = mock[HttpClient])
  val amlsRefNo            = "refNo"
  val accountTypeId        = ("foo", "bar")
  val accountType          = accountTypeId._1
  val accountId            = accountTypeId._2
  val statusResponse       = mock[ReadStatusResponse]

  "status" must {
    "successfully return a status" in {
      val getUrl = s"${amlsBackEndConnector.statusUrl}/$accountType/$accountId/$amlsRefNo/status"

      when {
        amlsBackEndConnector.httpClient.GET[ReadStatusResponse](eqTo(getUrl))(any(), any(), any())
      } thenReturn Future.successful(statusResponse)

      whenReady(amlsBackEndConnector.status(amlsRefNo, accountTypeId)) {
        _ mustBe statusResponse
      }
    }
  }
}