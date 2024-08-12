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

package forms.mappings

import generators.Generators
import org.scalacheck.Gen
import org.scalatest.OptionValues
import org.scalatest.freespec.AnyFreeSpec
import org.scalatest.matchers.must.Matchers
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.data.{Form, FormError}

import java.time.LocalDate

class DateMappingsSpec extends AnyFreeSpec with Matchers with ScalaCheckPropertyChecks with Generators with OptionValues
  with Mappings {

  val form = Form(
    "value" -> localDate(
      oneInvalidKey = "dateOfChange.error.invalid.one",
      multipleInvalidKey = "dateOfChange.error.invalid",
      oneRequiredKey = "dateOfChange.error.required",
      twoRequiredKey = "dateOfChange.error.required.two",
      allRequiredKey = "dateOfChange.error.required.all",
      realDateKey = "dateOfChange.error.real"
    )
  )

  val validData = datesBetween(
    min = LocalDate.of(2000, 1, 1),
    max = LocalDate.of(3000, 1, 1)
  )

  val invalidField: Gen[String] = Gen.alphaStr.suchThat(_.nonEmpty)

  val missingField: Gen[Option[String]] = Gen.option(Gen.const(""))

  "bind valid data" in {

    forAll(validData -> "valid date") {
      date =>

        val data = Map(
          "value.day" -> date.getDayOfMonth.toString,
          "value.month" -> date.getMonthValue.toString,
          "value.year" -> date.getYear.toString
        )

        val result = form.bind(data)

        result.value.value mustEqual date
    }
  }

  "fail to bind an empty date" in {

    val result = form.bind(Map.empty[String, String])

    result.errors must contain only FormError("value", "dateOfChange.error.required.all", List.empty)
  }

  "fail to bind a date with a missing day" in {

    forAll(validData -> "valid date", missingField -> "missing field") {
      (date, field) =>

        val initialData = Map(
          "value.month" -> date.getMonthValue.toString,
          "value.year" -> date.getYear.toString
        )

        val data = field.fold(initialData) {
          value =>
            initialData + ("value.day" -> value)
        }

        val result = form.bind(data)

        result.errors must contain only FormError("value.day", "dateOfChange.error.required", List("day"))
    }
  }

  "fail to bind a date with an invalid day" in {

    forAll(validData -> "valid date", invalidField -> "invalid field") {
      (date, field) =>

        val data = Map(
          "value.day" -> field,
          "value.month" -> date.getMonthValue.toString,
          "value.year" -> date.getYear.toString
        )

        val result = form.bind(data)

//        result.errors must contain(
//          FormError("value.day", "dateOfChange.error.invalid", List.empty)
//        )
        val formError = List(
          FormError("value.day", "dateOfChange.error.invalid.one", List("day"))
        )

        result.errors must contain theSameElementsAs formError
    }
  }

  "fail to bind a date with a missing month" in {

    forAll(validData -> "valid date", missingField -> "missing field") {
      (date, field) =>

        val initialData = Map(
          "value.day" -> date.getDayOfMonth.toString,
          "value.year" -> date.getYear.toString
        )

        val data = field.fold(initialData) {
          value =>
            initialData + ("value.month" -> value)
        }

        val result = form.bind(data)

        result.errors must contain only FormError("value.month", "dateOfChange.error.required", List("month"))
    }
  }

  "fail to bind a date with an invalid month" in {

    forAll(validData -> "valid data", invalidField -> "invalid field") {
      (date, field) =>

        val data = Map(
          "value.day" -> date.getDayOfMonth.toString,
          "value.month" -> field,
          "value.year" -> date.getYear.toString
        )
        val result = form.bind(data)

        val formError = List(
          FormError("value.month", "dateOfChange.error.invalid.one", List("month"))
        )

        result.errors must contain theSameElementsAs formError
    }
  }

  "fail to bind a date with a missing year" in {

    forAll(validData -> "valid date", missingField -> "missing field") {
      (date, field) =>

        val initialData = Map(
          "value.day" -> date.getDayOfMonth.toString,
          "value.month" -> date.getMonthValue.toString
        )

        val data = field.fold(initialData) {
          value =>
            initialData + ("value.year" -> value)
        }

        val result = form.bind(data)

        result.errors must contain only FormError("value.year", "dateOfChange.error.required", List("year"))
    }
  }

  "fail to bind a date with an invalid year" in {

    forAll(validData -> "valid data", invalidField -> "invalid field") {
      (date, field) =>

        val data = Map(
          "value.day" -> date.getDayOfMonth.toString,
          "value.month" -> date.getMonthValue.toString,
          "value.year" -> field
        )

        val result = form.bind(data)

        val formError = List(
          FormError("value.year", "dateOfChange.error.invalid.one", List("year"))
        )

        result.errors must contain theSameElementsAs formError
    }
  }

  "fail to bind a date with a missing day and month" in {

    forAll(validData -> "valid date", missingField -> "missing day", missingField -> "missing month") {
      (date, dayOpt, monthOpt) =>

        val day = dayOpt.fold(Map.empty[String, String]) {
          value =>
            Map("value.day" -> value)
        }

        val month = monthOpt.fold(Map.empty[String, String]) {
          value =>
            Map("value.month" -> value)
        }

        val data: Map[String, String] = Map(
          "value.year" -> date.getYear.toString
        ) ++ day ++ month

        val result = form.bind(data)

        val formError = List(
          FormError("value.day", "dateOfChange.error.required.two", List("day", "month")),
          FormError("value.month", "dateOfChange.error.required.two", List("day", "month"))
        )

        result.errors must contain theSameElementsAs formError
    }
  }

  "fail to bind a date with a missing day and year" in {

    forAll(validData -> "valid date", missingField -> "missing day", missingField -> "missing year") {
      (date, dayOpt, yearOpt) =>

        val day = dayOpt.fold(Map.empty[String, String]) {
          value =>
            Map("value.day" -> value)
        }

        val year = yearOpt.fold(Map.empty[String, String]) {
          value =>
            Map("value.year" -> value)
        }

        val data: Map[String, String] = Map(
          "value.month" -> date.getMonthValue.toString
        ) ++ day ++ year

        val result = form.bind(data)

        val formError = List(
          FormError("value.day", "dateOfChange.error.required.two", List("day", "year")),
          FormError("value.year", "dateOfChange.error.required.two", List("day", "year"))
        )

        result.errors must contain theSameElementsAs formError
    }
  }

  "fail to bind a date with a missing month and year" in {

    forAll(validData -> "valid date", missingField -> "missing month", missingField -> "missing year") {
      (date, monthOpt, yearOpt) =>

        val month = monthOpt.fold(Map.empty[String, String]) {
          value =>
            Map("value.month" -> value)
        }

        val year = yearOpt.fold(Map.empty[String, String]) {
          value =>
            Map("value.year" -> value)
        }

        val data: Map[String, String] = Map(
          "value.day" -> date.getDayOfMonth.toString
        ) ++ month ++ year

        val result = form.bind(data)

        val formError = List(
          FormError("value.month", "dateOfChange.error.required.two", List("month", "year")),
          FormError("value.year", "dateOfChange.error.required.two", List("month", "year"))
        )

        result.errors must contain theSameElementsAs formError
    }
  }

  "fail to bind an invalid day and month" in {

    forAll(validData -> "valid date", invalidField -> "invalid day", invalidField -> "invalid month") {
      (date, day, month) =>

        val data = Map(
          "value.day" -> day,
          "value.month" -> month,
          "value.year" -> date.getYear.toString
        )

        val result = form.bind(data)

        val formError = List(
          FormError("value.day", "dateOfChange.error.invalid", List("day", "month")),
          FormError("value.month", "dateOfChange.error.invalid", List("day", "month"))
        )

        result.errors must contain theSameElementsAs formError
    }
  }

  "fail to bind an invalid day and year" in {

    forAll(validData -> "valid date", invalidField -> "invalid day", invalidField -> "invalid year") {
      (date, day, year) =>

        val data = Map(
          "value.day" -> day,
          "value.month" -> date.getMonthValue.toString,
          "value.year" -> year
        )

        val result = form.bind(data)

        val formError = List(
          FormError("value.day", "dateOfChange.error.invalid", List("day", "year")),
          FormError("value.year", "dateOfChange.error.invalid", List("day", "year"))
        )

        result.errors must contain theSameElementsAs formError
    }
  }

  "fail to bind an invalid month and year" in {

    forAll(validData -> "valid date", invalidField -> "invalid month", invalidField -> "invalid year") {
      (date, month, year) =>

        val data = Map(
          "value.day" -> date.getDayOfMonth.toString,
          "value.month" -> month,
          "value.year" -> year
        )

        val result = form.bind(data)

        val formError = List(
          FormError("value.month", "dateOfChange.error.invalid", List("month", "year")),
          FormError("value.year", "dateOfChange.error.invalid", List("month", "year"))
        )

        result.errors must contain theSameElementsAs formError
    }
  }

  "fail to bind an invalid day, month and year" in {

    forAll(invalidField -> "valid day", invalidField -> "invalid month", invalidField -> "invalid year") {
      (day, month, year) =>

        val data = Map(
          "value.day" -> day,
          "value.month" -> month,
          "value.year" -> year
        )

        val result = form.bind(data)

        result.errors must contain only FormError("value", "dateOfChange.error.invalid", List.empty)
    }
  }

  "fail to bind an invalid date" in {

    val data = Map(
      "value.day" -> "30",
      "value.month" -> "2",
      "value.year" -> "2018"
    )

    val result = form.bind(data)

    result.errors must contain(
      FormError("value", "dateOfChange.error.real", List.empty)
    )
  }

  "unbind a date" in {

    forAll(validData -> "valid date") {
      date =>

        val filledForm = form.fill(date)

        filledForm("value.day").value.value mustEqual date.getDayOfMonth.toString
        filledForm("value.month").value.value mustEqual date.getMonthValue.toString
        filledForm("value.year").value.value mustEqual date.getYear.toString
    }
  }
}
