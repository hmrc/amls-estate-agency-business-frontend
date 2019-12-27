/*
 * Copyright 2019 HM Revenue & Customs
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
import controllers.routes
import play.api.Configuration
import play.api.i18n.Lang
import play.api.mvc.Call

@Singleton
class FrontendAppConfig @Inject() (configuration: Configuration) {

  private val contactHost = configuration.get[String]("contact-frontend.host")
  private val contactFormServiceIdentifier = "play26frontend"

  val timeoutSeconds = configuration.get[String](s"timeout.seconds")
  val timeoutCountdown = configuration.get[String](s"timeout.countdown")

  val analyticsToken: String = configuration.get[String](s"google-analytics.token")
  val analyticsHost: String = configuration.get[String](s"google-analytics.host")

  val reportAProblemPartialUrl = configuration.get[String]("microservice.services.contact-frontend.report-problem-url.with-js")
  val reportAProblemNonJSUrl = configuration.get[String]("microservice.services.contact-frontend.report-problem-url.non-js")
  val betaFeedbackUrl = configuration.get[String]("microservice.services.contact-frontend.beta-feedback-url.authenticated")
  val betaFeedbackUnauthenticatedUrl = configuration.get[String]("microservice.services.contact-frontend.beta-feedback-url.unauthenticated")

  val amlsFrontendBaseUrl = configuration.get[String](s"microservice.services.amls-frontend.url")

  val renewalProgressUrl = s"${amlsFrontendBaseUrl}/renewal-progress"
  val registrationProgressUrl = s"${amlsFrontendBaseUrl}/registration-progress"

  lazy val authUrl: String = configuration.get[Service]("auth").baseUrl
  lazy val loginUrl: String = configuration.get[String]("urls.login.url")
  lazy val logoutUrl: String = configuration.get[String]("urls.logout.url")
  lazy val loginContinueUrl: String = configuration.get[String]("urls.login.continue")

  lazy val languageTranslationEnabled: Boolean =
    configuration.get[Boolean]("microservice.services.features.welsh-translation")

  def languageMap: Map[String, Lang] = Map(
    "english" -> Lang("en"),
    "cymraeg" -> Lang("cy")
  )

  def routeToSwitchLanguage: String => Call =
    (lang: String) => routes.LanguageSwitchController.switchToLanguage(lang)
}
