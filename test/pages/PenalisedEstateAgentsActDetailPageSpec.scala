package pages

import pages.behaviours.PageBehaviours


class PenalisedEstateAgentsActDetailPageSpec extends PageBehaviours {

  "PenalisedEstateAgentsActDetailPage" must {

    beRetrievable[String](PenalisedEstateAgentsActDetailPage)

    beSettable[String](PenalisedEstateAgentsActDetailPage)

    beRemovable[String](PenalisedEstateAgentsActDetailPage)
  }
}
