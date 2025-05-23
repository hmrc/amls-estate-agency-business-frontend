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

package forms

import java.time.{LocalDate, ZoneOffset}

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form

class DateOfChangeFormProvider @Inject() extends Mappings {

  def apply(): Form[LocalDate] =
    Form(
      "value" -> localDate(
        oneInvalidKey = "dateOfChange.error.invalid",
        multipleInvalidKey = "dateOfChange.error.invalid",
        oneRequiredKey = "dateOfChange.error.required",
        twoRequiredKey = "dateOfChange.error.required.two",
        allRequiredKey = "dateOfChange.error.required.all",
        realDateKey = "dateOfChange.error.invalid"
      ).verifying(
        minDate(DateOfChangeFormProvider.pastDate, "dateOfChange.error.past"),
        maxDate(DateOfChangeFormProvider.futureDate, "dateOfChange.error.future")
      )
    )
}
object DateOfChangeFormProvider {

  def pastDate   = LocalDate.of(1900, 1, 1)
  def futureDate = LocalDate.now(ZoneOffset.UTC)
}
