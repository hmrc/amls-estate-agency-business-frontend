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

package connectors

import config.Service

import javax.inject.Inject
import models.ReadStatusResponse
import play.api.libs.json.{Json, Writes}
import play.api.{Configuration, Logging}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.http.{HttpClient}
import uk.gov.hmrc.http.HttpReads.Implicits.{readFromJson}

import scala.concurrent.{ExecutionContext, Future}

class AMLSBackEndConnector @Inject()(config: Configuration,
                                     implicit val httpClient: HttpClient) extends Logging {

  private val baseUrl = config.get[Service]("microservice.services.amls")
  private[connectors] val statusUrl = s"$baseUrl/amls/subscription"

  // GET status (API9)
  def status(amlsRegistrationNumber: String, accountTypeId: (String, String))
            (implicit headerCarrier: HeaderCarrier, ec: ExecutionContext, reqW: Writes[ReadStatusResponse]): Future[ReadStatusResponse] = {

    val (accountType, accountId) = accountTypeId

    val getUrl = s"$statusUrl/$accountType/$accountId/$amlsRegistrationNumber/status"

    httpClient.GET[ReadStatusResponse](getUrl) map {
      response =>
        // $COVERAGE-OFF$
        logger.debug(s"AmlsConnector:status - Response Body: ${Json.toJson(response)}")
        // $COVERAGE-ON$
        response
    }
  }
}