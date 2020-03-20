package pages

import java.time.LocalDate

import org.scalacheck.Arbitrary
import pages.behaviours.PageBehaviours

class DateOfChangePageSpec extends PageBehaviours {

  "DateOfChangePage" must {

    implicit lazy val arbitraryLocalDate: Arbitrary[LocalDate] = Arbitrary {
      datesBetween(LocalDate.of(1900, 1, 1), LocalDate.of(2100, 1, 1))
    }

    beRetrievable[LocalDate](DateOfChangePage)

    beSettable[LocalDate](DateOfChangePage)

    beRemovable[LocalDate](DateOfChangePage)
  }
}
