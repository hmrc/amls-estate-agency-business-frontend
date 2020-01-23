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

import javax.inject.{Inject, Singleton}
import play.api.mvc.Call
import controllers.routes
import models.EabServicesProvided.{Lettings, Residential}
import pages._
import models._

@Singleton
class Navigator @Inject()() {

  /*
  EAB Flow
  ————————————

  What you need =>
  Which services =>
    (If Residential or Lettings) Redress Scheme =>
      (If Other) Redress Scheme Detail =>
    (If Lettings) ClientMoneyProtectionScheme =>
  Penalised Estate Agents Act =>
    (If penalised) Penalised Estate Agents Act Details =>
  Penalised by Professional Body =>
    (If penalised) Penalised by Professional Body Details =>
  Check your answers
  */

  private val normalRoutes: Page => UserAnswers => Call = {
    case EabServicesProvidedPage             =>      redressSchemeRoute
    case RedressSchemePage                   =>      redressSchemeDetailRoute
    case ClientMoneyProtectionSchemePage     => _ => routes.PenalisedEstateAgentsActController.onPageLoad(NormalMode)
    case PenalisedEstateAgentsActPage        =>      penalisedEstateAgentsActDetailsRoute
    case PenalisedEstateAgentsActDetailPage  => _ => routes.PenalisedProfessionalBodyController.onPageLoad(NormalMode)
    case PenalisedProfessionalBodyPage       =>      penalisedProfessionalBodyDetailsRoute
    case PenalisedProfessionalBodyDetailPage => _ => routes.CheckYourAnswersController.onPageLoad()
    case _                                   => _ => routes.IndexController.onPageLoad()
  }

  private def redressSchemeRoute(answers: UserAnswers): Call = {
    answers.get(EabServicesProvidedPage) map { ans =>
      ans.contains(Residential) || ans.contains(Lettings) match {
        case true => routes.RedressSchemeController.onPageLoad(NormalMode)
        case _    => routes.PenalisedEstateAgentsActController.onPageLoad(NormalMode)
      }
    }
  }.getOrElse(routes.SessionExpiredController.onPageLoad())

  private def penalisedEstateAgentsActDetailsRoute(answers: UserAnswers): Call = {
    answers.get(PenalisedEstateAgentsActPage) match {
      case Some(true)  => routes.PenalisedEstateAgentsActDetailController.onPageLoad(NormalMode)
      case Some(false) => routes.PenalisedProfessionalBodyController.onPageLoad(NormalMode)
      case None        => routes.SessionExpiredController.onPageLoad()
    }
  }

  private def penalisedProfessionalBodyDetailsRoute(answers: UserAnswers): Call = {
    answers.get(PenalisedProfessionalBodyPage) match {
      case Some(true)  => routes.PenalisedProfessionalBodyDetailController.onPageLoad(NormalMode)
      case Some(false) => routes.CheckYourAnswersController.onPageLoad()
      case None        => routes.SessionExpiredController.onPageLoad()
    }
  }

  private def redressSchemeDetailRoute(answers: UserAnswers): Call = {
    (answers.get(RedressSchemePage), answers.get(EabServicesProvidedPage)) match {
      case (_, Some(services)) if services.contains(Lettings) => routes.ClientMoneyProtectionSchemeController.onPageLoad(NormalMode)
      case _ => routes.PenalisedEstateAgentsActController.onPageLoad(NormalMode)
    }
  }

  private def moneyProtectionSchemeRoute(answers: UserAnswers): Call = {
    answers.get(EabServicesProvidedPage) match {
      case Some(services) if services.contains(Lettings) => routes.ClientMoneyProtectionSchemeController.onPageLoad(NormalMode)
      case _        => routes.PenalisedEstateAgentsActController.onPageLoad(NormalMode)
    }
  }

  private val checkRouteMap: Page => UserAnswers => Call = {
    case EabServicesProvidedPage             =>      redressSchemeRouteCheckMode
    case RedressSchemePage                   =>      redressSchemeDetailRouteCheckMode
    case PenalisedEstateAgentsActPage        =>      penalisedEstateAgentsActDetailsRouteCheckMode
    case ClientMoneyProtectionSchemePage     => _ => routes.CheckYourAnswersController.onPageLoad()
    case PenalisedEstateAgentsActDetailPage  => _ => routes.CheckYourAnswersController.onPageLoad()
    case PenalisedProfessionalBodyPage       =>      penalisedProfessionalBodyDetailsRouteCheckMode
    case PenalisedProfessionalBodyDetailPage => _ => routes.CheckYourAnswersController.onPageLoad()
    case _                                   => _ => routes.CheckYourAnswersController.onPageLoad()
  }

  private def redressSchemeRouteCheckMode(answers: UserAnswers): Call = {
    answers.get(EabServicesProvidedPage) map { ans =>
      ans.contains(Residential) || ans.contains(Lettings) match {
        case true => routes.RedressSchemeController.onPageLoad(CheckMode)
        case _    => routes.CheckYourAnswersController.onPageLoad
      }
    }
  }.getOrElse(routes.SessionExpiredController.onPageLoad())

  private def moneyProtectionSchemeRouteCheckMode(answers: UserAnswers): Call = {
    answers.get(EabServicesProvidedPage) match {
      case Some(services) if services.contains(Lettings) => routes.ClientMoneyProtectionSchemeController.onPageLoad(CheckMode)
      case Some(_) => routes.PenalisedEstateAgentsActController.onPageLoad(CheckMode)
      case _           => routes.CheckYourAnswersController.onPageLoad
    }
  }

  private def redressSchemeDetailRouteCheckMode(answers: UserAnswers): Call = {
    answers.get(RedressSchemePage) match {
      case Some(_) if answers.get(EabServicesProvidedPage).exists(_.contains(Lettings)) => routes.ClientMoneyProtectionSchemeController.onPageLoad(CheckMode)
      case _                                                                            => routes.CheckYourAnswersController.onPageLoad
    }
  }

  private def penalisedEstateAgentsActDetailsRouteCheckMode(answers: UserAnswers): Call = {
    answers.get(PenalisedEstateAgentsActPage) match {
      case Some(true)  => routes.PenalisedEstateAgentsActDetailController.onPageLoad(CheckMode)
      case Some(false) => routes.CheckYourAnswersController.onPageLoad
      case None        => routes.SessionExpiredController.onPageLoad()
    }
  }

  private def penalisedProfessionalBodyDetailsRouteCheckMode(answers: UserAnswers): Call = {
    answers.get(PenalisedProfessionalBodyPage) match {
      case Some(true)  => routes.PenalisedProfessionalBodyDetailController.onPageLoad(CheckMode)
      case Some(false) => routes.CheckYourAnswersController.onPageLoad()
      case None        => routes.SessionExpiredController.onPageLoad()
    }
  }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode =>
      normalRoutes(page)(userAnswers)
    case CheckMode =>
      checkRouteMap(page)(userAnswers)
  }
}
