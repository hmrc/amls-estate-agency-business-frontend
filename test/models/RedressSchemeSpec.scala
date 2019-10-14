package models

import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import play.api.libs.json.{JsError, JsString, Json}

class RedressSchemeSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues {

  "RedressScheme" must {

    "deserialise valid values" in {

      val gen = Gen.oneOf(RedressScheme.values.toSeq)

      forAll(gen) {
        redressScheme =>

          JsString(redressScheme.toString).validate[RedressScheme].asOpt.value mustEqual redressScheme
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!RedressScheme.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[RedressScheme] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = Gen.oneOf(RedressScheme.values.toSeq)

      forAll(gen) {
        redressScheme =>

          Json.toJson(redressScheme) mustEqual JsString(redressScheme.toString)
      }
    }
  }
}
