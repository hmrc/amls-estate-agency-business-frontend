package pages

import models.RedressScheme
import pages.behaviours.PageBehaviours

class RedressSchemeSpec extends PageBehaviours {

  "RedressSchemePage" must {

    beRetrievable[RedressScheme](RedressSchemePage)

    beSettable[RedressScheme](RedressSchemePage)

    beRemovable[RedressScheme](RedressSchemePage)
  }
}
