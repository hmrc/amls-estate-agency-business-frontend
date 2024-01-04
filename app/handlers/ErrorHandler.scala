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

package handlers

import com.google.inject.Inject
import config.FrontendAppConfig
import play.api.i18n.MessagesApi
import play.api.mvc.Request
import uk.gov.hmrc.play.bootstrap.frontend.http.FrontendErrorHandler
import views.html.ErrorTemplate

class ErrorHandler @Inject()(val messagesApi: MessagesApi,
                                 val view: ErrorTemplate)
                                (implicit val appConfig: FrontendAppConfig) extends FrontendErrorHandler {

  override def standardErrorTemplate(pageTitle: String, heading: String, message: String)(implicit request: Request[_]) =
    view(pageTitle, heading, message)
}
