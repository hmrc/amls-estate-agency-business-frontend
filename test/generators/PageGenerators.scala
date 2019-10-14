package generators

import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

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
