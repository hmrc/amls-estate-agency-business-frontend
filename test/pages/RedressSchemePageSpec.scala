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

import models.RedressScheme.{OmbudsmanServices, Other}
import models.{RedressScheme, UserAnswers}
import pages.behaviours.PageBehaviours

class RedressSchemeSpec extends PageBehaviours {

  "RedressSchemePage" must {

    beRetrievable[RedressScheme](RedressSchemePage)

    beSettable[RedressScheme](RedressSchemePage)

    beRemovable[RedressScheme](RedressSchemePage)
  }

  "cleanup the RedressSchemeDetailPage value where not Other selected" in {
    val answerQuestion = UserAnswers("someid").set(RedressSchemeDetailPage, "sometext").success.value
    val updatedAnswers = answerQuestion.set(RedressSchemePage, OmbudsmanServices).success.value

    updatedAnswers.get(RedressSchemeDetailPage) must be(empty)
  }

  "not cleanup the RedressSchemeDetailPage value where true selected" in {
    val answerQuestion = UserAnswers("someid").set(RedressSchemeDetailPage, "sometext").success.value
    val updatedAnswers = answerQuestion.set(RedressSchemePage, Other).success.value

    updatedAnswers.get(RedressSchemePage) mustNot be(empty)
  }
}
