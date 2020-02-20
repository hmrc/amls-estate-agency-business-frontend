/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pages

import models.EabServicesProvided._
import models.RedressScheme._
import models.{EabServicesProvided, UserAnswers}
import pages.behaviours.PageBehaviours

class EabServicesProvidedPageSpec extends PageBehaviours {

  "EabServicesProvidedPage" must {

    beRetrievable[Seq[EabServicesProvided]](EabServicesProvidedPage)

    beSettable[Seq[EabServicesProvided]](EabServicesProvidedPage)

    beRemovable[Seq[EabServicesProvided]](EabServicesProvidedPage)
  }

  "cleanup the RedressSchemePage value where not Resident selected" in {
    val answerQuestion = UserAnswers("someid").set(RedressSchemePage, OmbudsmanServices).success.value
    val updatedAnswers = answerQuestion.set(EabServicesProvidedPage, Seq(AssetManagement)).success.value

    updatedAnswers.get(RedressSchemePage) must be(empty)
  }

  "not cleanup the RedressSchemePage value where true selected" in {
    val answerQuestion = UserAnswers("someid").set(RedressSchemePage, OmbudsmanServices).success.value
    val updatedAnswers = answerQuestion.set(EabServicesProvidedPage, Seq(Residential)).success.value

    updatedAnswers.get(RedressSchemePage) mustNot be(empty)
  }

  "cleanup the ClientMoneyProtectionSchemePage value where not Lettings selected" in {
    val answerQuestion = UserAnswers("someid").set(RedressSchemePage, OmbudsmanServices).success.value
    val updatedAnswers = answerQuestion.set(EabServicesProvidedPage, Seq(AssetManagement)).success.value

    updatedAnswers.get(ClientMoneyProtectionSchemePage) must be(empty)
  }

  "not cleanup the ClientMoneyProtectionSchemePage value where true selected" in {
    val answerQuestion = UserAnswers("someid").set(ClientMoneyProtectionSchemePage, true).success.value
    val updatedAnswers = answerQuestion.set(EabServicesProvidedPage, Seq(Lettings)).success.value

    updatedAnswers.get(ClientMoneyProtectionSchemePage) mustNot be(empty)
  }
}
