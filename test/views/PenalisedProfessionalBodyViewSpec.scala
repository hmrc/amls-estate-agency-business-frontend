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

package views

import forms.PenalisedProfessionalBodyFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.routing.Router.empty.routes
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.PenalisedProfessionalBodyView

class PenalisedProfessionalBodyViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "penalisedProfessionalBody"

  val supportingContent = messages("penalisedProfessionalBody.subtitle")

  val form = new PenalisedProfessionalBodyFormProvider()()

  "PenalisedProfessionalBody view" must {

    val view = viewFor[PenalisedProfessionalBodyView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like yesNoPage(form, applyView, messageKeyPrefix, controllers.routes.PenalisedProfessionalBodyController.onSubmit(NormalMode).url, Some(supportingContent))
  }
}
