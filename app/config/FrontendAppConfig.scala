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

package config

import com.google.inject.{Inject, Singleton}
import play.api.Configuration
import play.api.mvc.Request

import java.net.URLEncoder

@Singleton
class FrontendAppConfig @Inject() (configuration: Configuration) {

  private def baseUrl(serviceName: String): String = {
    val protocol = configuration.getOptional[String](s"microservice.services.$serviceName.protocol").getOrElse("http")
    val host = configuration.get[String](s"microservice.services.$serviceName.host")
    val port = configuration.get[String](s"microservice.services.$serviceName.port")
    s"$protocol://$host:$port"
  }

  val timeoutSeconds = configuration.get[String](s"timeout.seconds")
  val timeoutCountdown = configuration.get[String](s"timeout.countdown")
  def reportAProblemNonJSUrl(implicit request: Request[_]): String = {
    configuration.get[String]("microservice.services.contact-frontend.report-problem-url.non-js") +
    "&referrerUrl=" +
    URLEncoder.encode(baseUrl("amls-estate-agency-business-frontend") + request.uri, "utf-8")
  }

  val amlsFrontendBaseUrl = configuration.get[String](s"microservice.services.amls-frontend.url")

  val registrationProgressUrl = s"${amlsFrontendBaseUrl}/registration-progress"

  lazy val loginUrl: String = configuration.get[String]("urls.login.url")
  lazy val logoutUrl: String = configuration.get[String]("urls.logout.url")

}
