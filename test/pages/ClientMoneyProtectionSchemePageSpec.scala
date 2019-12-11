package pages

import pages.behaviours.PageBehaviours

class ClientMoneyProtectionSchemePageSpec extends PageBehaviours {

  "ClientMoneyProtectionSchemePage" must {

    beRetrievable[Boolean](ClientMoneyProtectionSchemePage)

    beSettable[Boolean](ClientMoneyProtectionSchemePage)

    beRemovable[Boolean](ClientMoneyProtectionSchemePage)
  }
}
