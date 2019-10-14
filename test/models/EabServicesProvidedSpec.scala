package models

import generators.ModelGenerators
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.Gen
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import play.api.libs.json.{JsError, JsString, Json}

class EabServicesProvidedSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {

  "EabServicesProvided" must {

    "deserialise valid values" in {

      val gen = arbitrary[EabServicesProvided]

      forAll(gen) {
        eabServicesProvided =>

          JsString(eabServicesProvided.toString).validate[EabServicesProvided].asOpt.value mustEqual eabServicesProvided
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!EabServicesProvided.values.map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[EabServicesProvided] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = arbitrary[EabServicesProvided]

      forAll(gen) {
        eabServicesProvided =>

          Json.toJson(eabServicesProvided) mustEqual JsString(eabServicesProvided.toString)
      }
    }
  }
}
