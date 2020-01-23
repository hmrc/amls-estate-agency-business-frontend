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

import models.UserAnswers
import pages.behaviours.PageBehaviours

class PenalisedProfessionalBodyPageSpec extends PageBehaviours {

  "PenalisedProfessionalBodyPage" must {

    beRetrievable[Boolean](PenalisedProfessionalBodyPage)

    beSettable[Boolean](PenalisedProfessionalBodyPage)

    beRemovable[Boolean](PenalisedProfessionalBodyPage)
  }

  "cleanup the PenalisedProfessionalBodyDetailPage value where false" in {
    val answerQuestion = UserAnswers("someid").set(PenalisedProfessionalBodyDetailPage, "sometext").success.value
    val updatedAnswers = answerQuestion.set(PenalisedProfessionalBodyPage, false).success.value

    updatedAnswers.get(PenalisedProfessionalBodyDetailPage) must be(empty)
  }

  "not cleanup the PenalisedProfessionalBodyDetailPage value where true" in {
    val answerQuestion = UserAnswers("someid").set(PenalisedProfessionalBodyDetailPage, "sometext").success.value
    val updatedAnswers = answerQuestion.set(PenalisedProfessionalBodyPage, true).success.value

    updatedAnswers.get(PenalisedProfessionalBodyDetailPage) must not be(empty)
  }
}
