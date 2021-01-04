/*
 * Copyright 2021 HM Revenue & Customs
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

import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

  implicit lazy val arbitraryDateOfChangePage: Arbitrary[DateOfChangePage.type] =
    Arbitrary(DateOfChangePage)

  implicit lazy val arbitraryClientMoneyProtectionSchemePage: Arbitrary[ClientMoneyProtectionSchemePage.type] =
    Arbitrary(ClientMoneyProtectionSchemePage)

  implicit lazy val arbitraryRedressSchemePage: Arbitrary[RedressSchemePage.type] =
    Arbitrary(RedressSchemePage)

  implicit lazy val arbitraryPenalisedProfessionalBodyDetailPage: Arbitrary[PenalisedProfessionalBodyDetailPage.type] =
    Arbitrary(PenalisedProfessionalBodyDetailPage)

  implicit lazy val arbitraryPenalisedProfessionalBodyPage: Arbitrary[PenalisedProfessionalBodyPage.type] =
    Arbitrary(PenalisedProfessionalBodyPage)

  implicit lazy val arbitraryPenalisedEstateAgentsActDetailPage: Arbitrary[PenalisedEstateAgentsActDetailPage.type] =
    Arbitrary(PenalisedEstateAgentsActDetailPage)

  implicit lazy val arbitraryPenalisedEstateAgentsActPage: Arbitrary[PenalisedEstateAgentsActPage.type] =
    Arbitrary(PenalisedEstateAgentsActPage)

  implicit lazy val arbitraryEabServicesProvidedPage: Arbitrary[EabServicesProvidedPage.type] =
    Arbitrary(EabServicesProvidedPage)
}
