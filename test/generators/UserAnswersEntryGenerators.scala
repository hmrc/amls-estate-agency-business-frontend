package generators

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryRedressSchemeUserAnswersEntry: Arbitrary[(RedressSchemePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RedressSchemePage.type]
        value <- arbitrary[RedressScheme].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPenalisedProfessionalBodyDetailUserAnswersEntry: Arbitrary[(PenalisedProfessionalBodyDetailPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PenalisedProfessionalBodyDetailPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPenalisedProfessionalBodyUserAnswersEntry: Arbitrary[(PenalisedProfessionalBodyPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PenalisedProfessionalBodyPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPenalisedEstateAgentsActDetailUserAnswersEntry: Arbitrary[(PenalisedEstateAgentsActDetailPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PenalisedEstateAgentsActDetailPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPenalisedEstateAgentsActUserAnswersEntry: Arbitrary[(PenalisedEstateAgentsActPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PenalisedEstateAgentsActPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryEabServicesProvidedUserAnswersEntry: Arbitrary[(EabServicesProvidedPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[EabServicesProvidedPage.type]
        value <- arbitrary[EabServicesProvided].map(Json.toJson(_))
      } yield (page, value)
    }
}
