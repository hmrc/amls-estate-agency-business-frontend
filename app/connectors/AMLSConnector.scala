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

import config.Service

import javax.inject.Inject
import models.{DateOfChangeResponse, UserAnswers}
import play.api.{Configuration, Logging}
import play.api.libs.json.{JsObject, Writes}
import uk.gov.hmrc.http.HttpReads.Implicits._
import uk.gov.hmrc.http.{HeaderCarrier, HttpClient, HttpReads, HttpResponse}
import uk.gov.hmrc.http.HttpReads.Implicits.{readFromJson, readRaw}


import scala.concurrent.{ExecutionContext, Future}

class AMLSConnector @Inject()(config: Configuration,
                              implicit val httpClient: HttpClient)
                             (implicit ec: ExecutionContext) extends Logging {

  private val baseUrl                 = config.get[Service]("microservice.services.amls-frontend")
  private[connectors] val url: String = s"$baseUrl/eab"

  def get(credId: String)(implicit hc: HeaderCarrier): Future[Option[JsObject]] = {
    val getUrl = s"$url/get/$credId"

    httpClient.GET[Option[JsObject]](getUrl)
  }

  def set(credId: String, userAnswers: UserAnswers)(implicit hc: HeaderCarrier)= {
    val putUrl = s"$url/set/$credId"

    httpClient.PUT(putUrl, userAnswers)(
      implicitly[Writes[UserAnswers]],
      implicitly[HttpReads[HttpResponse]],
      hc.withExtraHeaders("Csrf-Token" -> "nocheck"),
      implicitly
    )
  }

  def requireDateOfChange(credId: String,
                          submissionStatus: String,
                          userAnswers: UserAnswers)(implicit hc: HeaderCarrier) = {

    val postUrl = s"$url/require-date-change/$credId/$submissionStatus"

    httpClient.POST[UserAnswers, DateOfChangeResponse](postUrl, userAnswers)(
      implicitly[Writes[UserAnswers]],
      implicitly[HttpReads[DateOfChangeResponse]],
      hc.withExtraHeaders("Csrf-Token" -> "nocheck"),
      implicitly
    ) map {
      response =>
        // $COVERAGE-OFF$
        logger.debug(s"AMLSConnector:requireDateOfChange - Response: ${response}")
        // $COVERAGE-ON$
        response
    }
  }
}