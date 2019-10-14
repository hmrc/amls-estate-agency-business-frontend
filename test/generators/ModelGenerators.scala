package generators

import models._
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

trait ModelGenerators {

  implicit lazy val arbitraryRedressScheme: Arbitrary[RedressScheme] =
    Arbitrary {
      Gen.oneOf(RedressScheme.values.toSeq)
    }

  implicit lazy val arbitraryEabServicesProvided: Arbitrary[EabServicesProvided] =
    Arbitrary {
      Gen.oneOf(EabServicesProvided.values.toSeq)
    }
}
