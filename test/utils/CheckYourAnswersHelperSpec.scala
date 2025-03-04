/*
 * Copyright 2024 HM Revenue & Customs
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

package utils

import base.SpecBase
import models.UserAnswers
import org.jsoup.Jsoup
import play.api.libs.json.Json

class CheckYourAnswersHelperSpec extends SpecBase {

  val userAnswersSingleService: UserAnswers = UserAnswers(
    Json.obj(
      "eabServicesProvided"             -> Seq("businessTransfer"),
      "redressScheme"                   -> "propertyRedressScheme",
      "clientMoneyProtectionScheme"     -> false,
      "penalisedEstateAgentsAct"        -> true,
      "penalisedEstateAgentsActDetail"  -> "other eab details",
      "penalisedProfessionalBody"       -> false,
      "penalisedProfessionalBodyDetail" -> "other pb details"
    )
  )

  val userAnswersMultipleServices: UserAnswers = UserAnswers(
    Json.obj(
      "eabServicesProvided"             -> Seq("auctioneering", "businessTransfer"),
      "redressScheme"                   -> "propertyRedressScheme",
      "clientMoneyProtectionScheme"     -> false,
      "penalisedEstateAgentsAct"        -> true,
      "penalisedEstateAgentsActDetail"  -> "other eab details",
      "penalisedProfessionalBody"       -> false,
      "penalisedProfessionalBodyDetail" -> "other pb details"
    )
  )

  "CheckYourAnswersHelper" must {
    "have eabServicesProvided method which" when {
      "called for single service" must {
        "return single answer row with un-bulleted content" in {

          val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswersSingleService)(messages)
          val answers                = checkYourAnswersHelper.eabServicesProvided.value
          val doc                    = Jsoup.parse(answers.toString)

          doc.getElementsByClass("list list-bullet").size() mustBe 0
          doc.toString must include("Business transfer")

        }
      }

      "called for multiple services" must {
        "return multiple answer row with bulleted content" in {

          val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswersMultipleServices)(messages)
          val answers                = checkYourAnswersHelper.eabServicesProvided.value
          val doc                    = Jsoup.parse(answers.toString)

          doc.getElementsByClass("govuk-list govuk-list--bullet").size() mustBe 1
          doc.getElementsByClass("govuk-list govuk-list--bullet").toString must (include("Auctioneering") and include(
            "Business transfer"
          ))
        }
      }
    }

    "have clientMoneyProtectionScheme method which" when {
      "called for single service" must {
        "return single answer row with un-bulleted content" in {

          val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswersSingleService)(messages)
          val answers                = checkYourAnswersHelper.clientMoneyProtectionScheme.value
          val doc                    = Jsoup.parse(answers.toString)

          doc.getElementsByClass("list list-bullet").size() mustBe 0
          doc.toString must include("No")
        }
      }
    }

    "have redressScheme method which" when {
      "called for single service" must {
        "return single answer row with un-bulleted content" in {

          val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswersSingleService)(messages)
          val answers                = checkYourAnswersHelper.redressScheme.value
          val doc                    = Jsoup.parse(answers.toString)

          doc.getElementsByClass("list list-bullet").size() mustBe 0
          doc.toString must include("Property Redress Scheme")
        }
      }
    }

    "have penalisedEstateAgentsAct method which" when {
      "called for single service" must {
        "return single answer row with un-bulleted content" in {

          val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswersSingleService)(messages)
          val answers                = checkYourAnswersHelper.penalisedEstateAgentsAct.value
          val doc                    = Jsoup.parse(answers.toString)

          doc.getElementsByClass("list list-bullet").size() mustBe 0
          doc.toString must include("Yes")
        }
      }
    }

    "have penalisedEstateAgentsActDetail method which" when {
      "called for single service" must {
        "return single answer row with un-bulleted content" in {

          val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswersSingleService)(messages)
          val answers                = checkYourAnswersHelper.penalisedEstateAgentsActDetail.value
          val doc                    = Jsoup.parse(answers.toString)

          doc.getElementsByClass("list list-bullet").size() mustBe 0
          doc.toString must include("other eab details")
        }
      }
    }

    "have penalisedProfessionalBody method which" when {
      "called for single service" must {
        "return single answer row with un-bulleted content" in {

          val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswersSingleService)(messages)
          val answers                = checkYourAnswersHelper.penalisedProfessionalBody.value
          val doc                    = Jsoup.parse(answers.toString)

          doc.getElementsByClass("list list-bullet").size() mustBe 0
          doc.toString must include("No")
        }
      }
    }

    "have penalisedProfessionalBodyDetail method which" when {
      "called for single service" must {
        "return single answer row with un-bulleted content" in {

          val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswersSingleService)(messages)
          val answers                = checkYourAnswersHelper.penalisedProfessionalBodyDetail.value
          val doc                    = Jsoup.parse(answers.toString)

          doc.getElementsByClass("list list-bullet").size() mustBe 0
          doc.toString must include("other pb details")
        }
      }
    }
  }
}
