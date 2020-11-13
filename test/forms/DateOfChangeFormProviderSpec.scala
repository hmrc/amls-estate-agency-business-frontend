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

package forms

import forms.behaviours.DateBehaviours
import play.api.data.FormError

class DateOfChangeFormProviderSpec extends DateBehaviours {

  val form         = new DateOfChangeFormProvider()()
  val minDateError = "dateOfChange.error.past"
  val maxDateError = "dateOfChange.error.future"

  ".value" should {

    val validData = datesBetween(
      min = DateOfChangeFormProvider.pastDate,
      max = DateOfChangeFormProvider.futureDate
    )

    behave like dateField(form, "value", validData)
    behave like mandatoryDateField(form, "value", "dateOfChange.error.required.all")
    behave like dateFieldWithMin(form, "value", DateOfChangeFormProvider.pastDate, FormError("value", minDateError))
    behave like dateFieldWithMax(form, "value", DateOfChangeFormProvider.futureDate, FormError("value", maxDateError))

    "fail to bind a date with a missing value" in {

      val formError = FormError("value", "dateOfChange.error.required", List("month"))

      val validDate = DateOfChangeFormProvider.pastDate

      val data = Map(
        "value.day"   -> validDate.getDayOfMonth.toString,
        "value.month" -> "",
        "value.year"  -> validDate.getYear.toString
      )

      val result = form.bind(data)
      result.errors should contain only formError
    }

    "fail to bind a date with two missing values" in {

      val formError = FormError("value", "dateOfChange.error.required.two", List("month", "year"))

      val validDate = DateOfChangeFormProvider.pastDate

      val data = Map(
        "value.day"   -> validDate.getDayOfMonth.toString,
        "value.month" -> "",
        "value.year"  -> ""
      )

      val result = form.bind(data)
      result.errors should contain only formError
    }
  }
}
