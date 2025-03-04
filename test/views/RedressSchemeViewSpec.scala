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

package views

import forms.RedressSchemeFormProvider
import models.{NormalMode, RedressScheme}
import play.api.data.Form
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.Aliases.RadioItem
import views.behaviours.ViewBehaviours
import views.html.RedressSchemeView

class RedressSchemeViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "redressScheme"

  val form = new RedressSchemeFormProvider()()

  val view = viewFor[RedressSchemeView](Some(emptyUserAnswers))

  def applyView(form: Form[RedressScheme]): HtmlFormat.Appendable =
    view.apply(form, NormalMode)(fakeRequest, messages)

  "RedressSchemeView" must {

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))
  }

  "RedressSchemeView" when {

    "rendered" must {

      "contain radio buttons for the value" when {

        val doc = asDocument(applyView(form))
        for (option <- RedressScheme.options)
          assertContainsRadioButton(doc, option.id.value, "value", option.value.value, false)

        "contain supporting content for the question" in {
          val doc               = asDocument(applyView(form))
          val supportingContent = doc.getElementsContainingOwnText(messages("redressSchemeDetail.content"))
          supportingContent.size mustBe 1
        }
      }

      RedressScheme.options.zipWithIndex.foreach { case (option: RadioItem, i: Int) =>
        s"rendered with a value of '${option.value.value}'" must {

          s"have the '${option.value.value}' radio button selected" in {

            val formWithRadioSelected = form.fill(RedressScheme.values(i))

            def doc = asDocument(applyView(formWithRadioSelected))

            assertContainsRadioButton(doc, option.id.value, "value", option.value.value, isChecked = true)

            for (unselectedOption <- RedressScheme.options.filterNot(o => o == option))
              assertContainsRadioButton(
                doc,
                unselectedOption.id.value,
                "value",
                unselectedOption.value.value,
                isChecked = false
              )
          }
        }
      }
    }
  }
}
