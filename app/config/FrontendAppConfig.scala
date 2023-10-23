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

@Singleton
class FrontendAppConfig @Inject() (configuration: Configuration) {

  val timeoutSeconds = configuration.get[Int](s"timeout.seconds")
  val timeoutCountdown = configuration.get[Int](s"timeout.countdown")

  val amlsFrontendBaseUrl = configuration.get[String](s"microservice.services.amls-frontend.url")

  val registrationProgressUrl = s"${amlsFrontendBaseUrl}/registration-progress"

  lazy val loginUrl: String = configuration.get[String]("urls.login.url")
  lazy val logoutUrl: String = configuration.get[String]("urls.logout.url")

}
