/*
 * Copyright 2021 HM Revenue & Customs
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

import models.UserAnswers
import pages.behaviours.PageBehaviours

class PenalisedEstateAgentsActPageSpec extends PageBehaviours {

  "PenalisedEstateAgentsActPage" must {

    beRetrievable[Boolean](PenalisedEstateAgentsActPage)

    beSettable[Boolean](PenalisedEstateAgentsActPage)

    beRemovable[Boolean](PenalisedEstateAgentsActPage)
  }

  "cleanup the PenalisedEstateAgentsDetailPage value where false" in {
    val answerQuestion = UserAnswers().set(PenalisedEstateAgentsActDetailPage, "sometext").success.value
    val updatedAnswers = answerQuestion.set(PenalisedEstateAgentsActPage, false).success.value

    updatedAnswers.get(PenalisedEstateAgentsActDetailPage) must be(empty)
  }

  "not cleanup the PenalisedEstateAgentsDetailPage value where true" in {
    val answerQuestion = UserAnswers().set(PenalisedEstateAgentsActDetailPage, "sometext").success.value
    val updatedAnswers = answerQuestion.set(PenalisedEstateAgentsActPage, true).success.value

    updatedAnswers.get(PenalisedEstateAgentsActDetailPage) must not be(empty)
  }
}
