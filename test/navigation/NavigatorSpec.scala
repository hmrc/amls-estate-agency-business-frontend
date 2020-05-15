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

package navigation

import base.SpecBase
import controllers.routes
import models.EabServicesProvided._
import models.RedressScheme._
import pages._
import models._

class NavigatorSpec extends SpecBase {

  val navigator = new Navigator

  "Navigator" when {
    "in Normal mode" must {
      "go from redress scheme to Penalised Estate Agents Act if residential" in {
        val eabServices = UserAnswers().set(EabServicesProvidedPage, Seq(Residential)).success.value
        val answersRedress = eabServices.set(RedressSchemePage, ThePropertyOmbudsman).success.value

        navigator.nextPage(RedressSchemePage, NormalMode, answersRedress)
          .mustBe(routes.PenalisedEstateAgentsActController.onPageLoad(NormalMode))
      }

      "go from redress scheme to Money Protection Scheme if lettings" in {
        val eabServices = UserAnswers().set(EabServicesProvidedPage, Seq(Lettings)).success.value
        val answersRedress = eabServices.set(RedressSchemePage, ThePropertyOmbudsman).success.value

        navigator.nextPage(RedressSchemePage, NormalMode, answersRedress)
          .mustBe(routes.ClientMoneyProtectionSchemeController.onPageLoad(NormalMode))
      }

      "go from Money Protection Scheme to Penalised Estate Agents Act" in {
        navigator.nextPage(ClientMoneyProtectionSchemePage, NormalMode, UserAnswers())
          .mustBe(routes.PenalisedEstateAgentsActController.onPageLoad(NormalMode))
      }

      "go from which services to redress scheme if residential" in {
        val answers = UserAnswers().set(EabServicesProvidedPage, Seq(Residential)).success.value

        navigator.nextPage(EabServicesProvidedPage, NormalMode, answers)
          .mustBe(routes.RedressSchemeController.onPageLoad(NormalMode))
      }

      "go from which services to redress scheme if lettings" in {
        val answers = UserAnswers().set(EabServicesProvidedPage, Seq(Lettings)).success.value

        navigator.nextPage(EabServicesProvidedPage, NormalMode, answers)
          .mustBe(routes.RedressSchemeController.onPageLoad(NormalMode))
      }

      "go from which services to Penalised Estate Agents Act if not residential" in {
        val answers = UserAnswers().set(EabServicesProvidedPage, Seq(AssetManagement)).success.value

        navigator.nextPage(EabServicesProvidedPage, NormalMode, answers)
          .mustBe(routes.PenalisedEstateAgentsActController.onPageLoad(NormalMode))
      }

      "go from redress scheme to Penalised Estate Agents Act if redress scheme not other" in {
        val answers = UserAnswers().set(RedressSchemePage, ThePropertyOmbudsman).success.value

        navigator.nextPage(RedressSchemePage, NormalMode, answers)
          .mustBe(routes.PenalisedEstateAgentsActController.onPageLoad(NormalMode))
      }

      "go from Penalised Estate Agents Act to Penalised Estate Agents Act Detail if true" in {
        val answers = UserAnswers().set(PenalisedEstateAgentsActPage, true).success.value

        navigator.nextPage(PenalisedEstateAgentsActPage, NormalMode, answers)
          .mustBe(routes.PenalisedEstateAgentsActDetailController.onPageLoad(NormalMode))
      }

      "go from Penalised Estate Agents Act to Penalised by Professional Body if false" in {
        val answers = UserAnswers().set(PenalisedEstateAgentsActPage, false).success.value

        navigator.nextPage(PenalisedEstateAgentsActPage, NormalMode, answers)
          .mustBe(routes.PenalisedProfessionalBodyController.onPageLoad(NormalMode))
      }

      "go from Penalised Estate Agents Act Detail to Penalised by Professional Body" in {
        navigator.nextPage(PenalisedEstateAgentsActDetailPage, NormalMode, UserAnswers())
          .mustBe(routes.PenalisedProfessionalBodyController.onPageLoad(NormalMode))
      }

      "go from Penalised by Professional Body to Penalised by Professional Body Detail where true" in {
        val answers = UserAnswers().set(PenalisedProfessionalBodyPage, true).success.value

        navigator.nextPage(PenalisedProfessionalBodyPage, NormalMode, answers)
          .mustBe(routes.PenalisedProfessionalBodyDetailController.onPageLoad(NormalMode))
      }

      "go from Penalised by Professional Body to Check Your Answers where false" in {
        val answers = UserAnswers().set(PenalisedProfessionalBodyPage, false).success.value

        navigator.nextPage(PenalisedProfessionalBodyPage, NormalMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from Penalised by Professional Body Detail to Check Your Answers" in {
        navigator.nextPage(PenalisedProfessionalBodyDetailPage, NormalMode, UserAnswers())
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }
    }

    "in Check mode" must {

      "go to CheckYourAnswers from a page that doesn't exist in the edit route map" in {
        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, UserAnswers()) mustBe routes.CheckYourAnswersController.onPageLoad()
      }

      "go from which services to date of change" in {
        val answers = UserAnswers()

        navigator.nextPage(EabServicesProvidedPage, CheckMode, answers)
          .mustBe(routes.DateOfChangeController.onPageLoad(CheckMode))
      }

      "go from date of change to redress scheme if residential and residential not answered" in {
        val answers = UserAnswers().set(EabServicesProvidedPage, Seq(Residential)).success.value

        navigator.nextPage(DateOfChangePage, CheckMode, answers)
          .mustBe(routes.RedressSchemeController.onPageLoad(CheckMode))
      }

      "go from date of change to check your answers if residential and residential answered" in {
        val answersWithRedress = UserAnswers().set(RedressSchemePage, ThePropertyOmbudsman).success.value
        val answers = answersWithRedress.set(EabServicesProvidedPage, Seq(Residential)).success.value

        navigator.nextPage(DateOfChangePage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from date of change to redress scheme if lettings and client money protection not answered" in {
        val answers = UserAnswers().set(EabServicesProvidedPage, Seq(Lettings)).success.value

        navigator.nextPage(DateOfChangePage, CheckMode, answers)
          .mustBe(routes.RedressSchemeController.onPageLoad(CheckMode))
      }

      "go from date of change to check your answers if lettings and client money protection answered" in {
        val answersWithCmp = UserAnswers().set(ClientMoneyProtectionSchemePage, true).success.value
        val answers = answersWithCmp.set(EabServicesProvidedPage, Seq(Lettings)).success.value

        navigator.nextPage(DateOfChangePage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from date of change to Check Your Answers if not residential" in {
        val answers = UserAnswers().set(EabServicesProvidedPage, Seq(AssetManagement)).success.value

        navigator.nextPage(DateOfChangePage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from redress scheme to Check Your Answers if lettings not selected" in {
        val answersWithLettings = UserAnswers().set(EabServicesProvidedPage, Seq(AssetManagement)).success.value
        val answers = answersWithLettings.set(RedressSchemePage, NotRegistered).success.value

        navigator.nextPage(RedressSchemePage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from redress scheme to Client Money Protection if lettings selected and CMP not answered" in {
        val answersWithLettings = UserAnswers().set(EabServicesProvidedPage, Seq(Lettings)).success.value
        val answers = answersWithLettings.set(RedressSchemePage, NotRegistered).success.value

        navigator.nextPage(RedressSchemePage, CheckMode, answers)
          .mustBe(routes.ClientMoneyProtectionSchemeController.onPageLoad(CheckMode))
      }

      "go from redress scheme to Check Your Answers if lettings selected and CMP answered" in {
        val answersWithLettings = UserAnswers().set(EabServicesProvidedPage, Seq(Lettings)).success.value
        val answersWithCmp = answersWithLettings.set(ClientMoneyProtectionSchemePage, true).success.value
        val answers = answersWithCmp.set(RedressSchemePage, NotRegistered).success.value

        navigator.nextPage(RedressSchemePage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from Penalised Estate Agents Act to Penalised Estate Agents Act Detail if true" in {
        val answers = UserAnswers().set(PenalisedEstateAgentsActPage, true).success.value

        navigator.nextPage(PenalisedEstateAgentsActPage, CheckMode, answers)
          .mustBe(routes.PenalisedEstateAgentsActDetailController.onPageLoad(CheckMode))
      }

      "go from Penalised Estate Agents Act to Check Your Answers if false" in {
        val answers = UserAnswers().set(PenalisedEstateAgentsActPage, false).success.value

        navigator.nextPage(PenalisedEstateAgentsActPage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from Penalised Estate Agents Act Detail to Check Your Answers" in {
        navigator.nextPage(PenalisedEstateAgentsActDetailPage, CheckMode, UserAnswers())
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from Penalised by Professional Body to Penalised by Professional Body Detail where true" in {
        val answers = UserAnswers().set(PenalisedProfessionalBodyPage, true).success.value

        navigator.nextPage(PenalisedProfessionalBodyPage, CheckMode, answers)
          .mustBe(routes.PenalisedProfessionalBodyDetailController.onPageLoad(CheckMode))
      }

      "go from Penalised by Professional Body to Check Your Answers where false" in {
        val answers = UserAnswers().set(PenalisedProfessionalBodyPage, false).success.value

        navigator.nextPage(PenalisedProfessionalBodyPage, CheckMode, answers)
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }

      "go from Penalised by Professional Body Detail to Check Your Answers" in {
        navigator.nextPage(PenalisedProfessionalBodyDetailPage, CheckMode, UserAnswers())
          .mustBe(routes.CheckYourAnswersController.onPageLoad())
      }
    }
  }
}
