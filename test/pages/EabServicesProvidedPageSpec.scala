package pages

import models.EabServicesProvided
import pages.behaviours.PageBehaviours

class EabServicesProvidedPageSpec extends PageBehaviours {

  "EabServicesProvidedPage" must {

    beRetrievable[Set[EabServicesProvided]](EabServicesProvidedPage)

    beSettable[Set[EabServicesProvided]](EabServicesProvidedPage)

    beRemovable[Set[EabServicesProvided]](EabServicesProvidedPage)
  }
}
