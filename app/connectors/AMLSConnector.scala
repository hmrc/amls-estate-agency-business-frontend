/*
 * Copyright 2020 HM Revenue & Customs
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
import models.UserAnswers
import play.api.{Configuration, Logger}
import play.api.libs.json.{JsObject, Json, Writes}
import uk.gov.hmrc.http.{HeaderCarrier, HttpReads, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class AMLSConnector @Inject()(config: Configuration,
                              implicit val httpClient: HttpClient)
                             (implicit ec: ExecutionContext) {

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
                          isSubmitted: Boolean,
                          userAnswers: UserAnswers)
                         (implicit hc: HeaderCarrier) = {

    val postUrl = s"$url/require-date-change/$credId/$isSubmitted"

    httpClient.POST[UserAnswers, JsObject](postUrl, userAnswers)(
      implicitly[Writes[UserAnswers]],
      implicitly[HttpReads[JsObject]],
      hc.withExtraHeaders("Csrf-Token" -> "nocheck"),
      implicitly
    ) map {
      response =>
        // $COVERAGE-OFF$
        Logger.debug(s"requireDateOfChange - Response Body: ${Json.toJson(response)}")
        response
    }
  }
}