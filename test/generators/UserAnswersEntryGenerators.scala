/*
 * Copyright 2019 HM Revenue & Customs
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
