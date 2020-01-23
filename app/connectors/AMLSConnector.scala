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
import play.api.Configuration
import play.api.libs.json.JsObject
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class AMLSConnector @Inject()(config: Configuration,
                              implicit val httpClient: HttpClient)
                             (implicit ec: ExecutionContext) {

  private val baseUrl                 = config.get[Service]("microservice.services.amls-frontend")
  private[connectors] val url: String = s"$baseUrl/eab"

  def get(id: String)(implicit hc: HeaderCarrier): Future[Option[JsObject]] = {
    val getUrl = s"$url/$id"
    httpClient.GET[Option[JsObject]](getUrl)
  }

  def set(id: String, userAnswers: UserAnswers)(implicit hc: HeaderCarrier): Future[UserAnswers] = {
    val putUrl = s"$url/$id"
    httpClient.PUT[UserAnswers, UserAnswers](putUrl, userAnswers)
  }

}
