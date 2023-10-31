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

import forms.mappings.Mappings
import play.api.data.{Field, FormError, _}
import views.html.components.input_date

class inputDateSpec extends ViewSpecBase with Mappings {

  "input_date view" must {
    "render correct aria-describedby attribute values (error only)" in {
      val form = Form("value" -> text("custom.error"))
      val field = Field(form, "field", constraints = Seq(), format = None, errors = Seq(FormError("error", "error.message")), value = None)
      val inputDate = asDocument(input_date(field = field, legend = "someLegend")())
      val aria = inputDate.getElementsByTag("fieldset").attr("aria-describedby")
      aria must be("error-message-field-input")
    }

    "render correct aria-describedby attribute values (error and hint)" in {
      val form = Form("value" -> text("custom.error"))
      val field = Field(form, "field", constraints = Seq(), format = None, errors = Seq(FormError("error", "error.message")), value = None)
      val inputDate = asDocument(input_date(field = field, legend = "someLegend", hint = Some("hint"))())
      val aria = inputDate.getElementsByTag("fieldset").attr("aria-describedby")
      aria must be("error-message-field-input hint-field")
    }

    "render correct aria-describedby attribute values (hint only)" in {
      val form = Form("value" -> text("custom.error"))
      val field = Field(form, "field", constraints = Seq(), format = None, errors = Seq(), value = None)
      val inputDate = asDocument(input_date(field = field, legend = "someLegend", hint = Some("hint"))())
      val aria = inputDate.getElementsByTag("fieldset").attr("aria-describedby")
      aria must be("hint-field")
    }
  }
}
