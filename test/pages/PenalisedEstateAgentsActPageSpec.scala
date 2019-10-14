package pages

import pages.behaviours.PageBehaviours

class PenalisedEstateAgentsActPageSpec extends PageBehaviours {

  "PenalisedEstateAgentsActPage" must {

    beRetrievable[Boolean](PenalisedEstateAgentsActPage)

    beSettable[Boolean](PenalisedEstateAgentsActPage)

    beRemovable[Boolean](PenalisedEstateAgentsActPage)
  }
}
