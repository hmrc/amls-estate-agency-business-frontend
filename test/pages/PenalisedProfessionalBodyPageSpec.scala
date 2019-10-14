package pages

import pages.behaviours.PageBehaviours

class PenalisedProfessionalBodyPageSpec extends PageBehaviours {

  "PenalisedProfessionalBodyPage" must {

    beRetrievable[Boolean](PenalisedProfessionalBodyPage)

    beSettable[Boolean](PenalisedProfessionalBodyPage)

    beRemovable[Boolean](PenalisedProfessionalBodyPage)
  }
}
