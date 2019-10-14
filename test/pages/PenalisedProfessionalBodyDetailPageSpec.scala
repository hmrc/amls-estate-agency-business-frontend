package pages

import pages.behaviours.PageBehaviours


class PenalisedProfessionalBodyDetailPageSpec extends PageBehaviours {

  "PenalisedProfessionalBodyDetailPage" must {

    beRetrievable[String](PenalisedProfessionalBodyDetailPage)

    beSettable[String](PenalisedProfessionalBodyDetailPage)

    beRemovable[String](PenalisedProfessionalBodyDetailPage)
  }
}
