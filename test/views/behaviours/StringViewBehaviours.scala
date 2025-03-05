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

package views.behaviours

import play.api.data.Form
import play.twirl.api.HtmlFormat

trait StringViewBehaviours extends QuestionViewBehaviours[String] {

  val answer = "answer"

  def stringPage(
    form: Form[String],
    createView: Form[String] => HtmlFormat.Appendable,
    messageKeyPrefix: String,
    expectedHintKey: Option[String] = None
  ) =
    "behave like a page with a string value field" when {

      "rendered" must {

        "contain a label for the value" in {
          val doc              = asDocument(createView(form))
          val expectedHintText = expectedHintKey map (k => messages(k))
          assertContainsLabel(doc, "value", messages(s"$messageKeyPrefix.heading"), expectedHintText)
        }

        "contain an input for the value" in {
          val doc = asDocument(createView(form))
          assertRenderedById(doc, "value")
        }
      }

      "rendered with an error" must {
        "show an error summary" in {
          val doc = asDocument(createView(form.withError(error)))
          assertRenderedByCssSelector(doc, ".govuk-error-summary")
        }

        "show an error associated to the value field" in {
          val doc       = asDocument(createView(form.withError(error)))
          val errorSpan = doc.getElementById("value-error")
          errorSpan.text mustBe (messages("error.browser.title.prefix") + " " + messages(errorMessage))
        }

        "show an error prefix in the browser title" in {
          val doc = asDocument(createView(form.withError(error)))
          assertEqualsValue(
            doc,
            "title",
            s"""${messages("error.browser.title.prefix")} ${messages(
                s"$messageKeyPrefix.title"
              )} - Estate agency business - Manage your anti-money laundering supervision - GOV.UK"""
          )
        }
      }
    }

  def textareaPage(
    form: Form[String],
    createView: Form[String] => HtmlFormat.Appendable,
    messageKeyPrefix: String,
    expectedFormAction: String,
    expectedHintKey: Option[String] = None
  ) = {

    stringPage(form, createView, messageKeyPrefix, expectedHintKey)

    "behave like a page with a text area" when {
      "rendered with a valid form" must {
        "include the form's value in the value input" in {
          val doc = asDocument(createView(form.fill(answer)))
          doc.getElementById("value").text mustBe answer
        }
      }
    }
  }

  def textfieldPage(
    form: Form[String],
    createView: Form[String] => HtmlFormat.Appendable,
    messageKeyPrefix: String,
    expectedFormAction: String,
    expectedHintKey: Option[String] = None
  ) = {

    stringPage(form, createView, messageKeyPrefix, expectedHintKey)

    "behave like a page with a text field" when {
      "rendered with a valid form" must {
        "include the form's value in the value input" in {
          val doc = asDocument(createView(form.fill(answer)))
          doc.getElementById("value").text mustBe answer
        }
      }
    }
  }
}
