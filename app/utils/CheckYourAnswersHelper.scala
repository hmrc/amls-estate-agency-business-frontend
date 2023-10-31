/*
 * Copyright 2023 HM Revenue & Customs
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

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages._
import play.api.i18n.Messages
import play.twirl.api.{Html, HtmlFormat}
import viewmodels.AnswerRow

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  def clientMoneyProtectionScheme: Option[AnswerRow] = userAnswers.get(ClientMoneyProtectionSchemePage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("clientMoneyProtectionScheme.checkYourAnswersLabel")),
        yesOrNo(x),
        routes.ClientMoneyProtectionSchemeController.onPageLoad(CheckMode).url
      )
  }

  def redressScheme: Option[AnswerRow] = userAnswers.get(RedressSchemePage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("redressScheme.checkYourAnswersLabel")),
        HtmlFormat.escape(messages(s"redressScheme.$x")),
        routes.RedressSchemeController.onPageLoad(CheckMode).url
      )
  }

  def penalisedProfessionalBodyDetail: Option[AnswerRow] = userAnswers.get(PenalisedProfessionalBodyDetailPage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("penalisedProfessionalBodyDetail.checkYourAnswersLabel")),
        HtmlFormat.escape(x),
        routes.PenalisedProfessionalBodyDetailController.onPageLoad(CheckMode).url
      )
  }

  def penalisedProfessionalBody: Option[AnswerRow] = userAnswers.get(PenalisedProfessionalBodyPage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("penalisedProfessionalBody.checkYourAnswersLabel")),
        yesOrNo(x),
        routes.PenalisedProfessionalBodyController.onPageLoad(CheckMode).url
      )
  }

  def penalisedEstateAgentsActDetail: Option[AnswerRow] = userAnswers.get(PenalisedEstateAgentsActDetailPage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("penalisedEstateAgentsActDetail.checkYourAnswersLabel")),
        HtmlFormat.escape(x),
        routes.PenalisedEstateAgentsActDetailController.onPageLoad(CheckMode).url
      )
  }

  def penalisedEstateAgentsAct: Option[AnswerRow] = userAnswers.get(PenalisedEstateAgentsActPage) map {
    x =>
      AnswerRow(
        HtmlFormat.escape(messages("penalisedEstateAgentsAct.checkYourAnswersLabel")),
        yesOrNo(x),
        routes.PenalisedEstateAgentsActController.onPageLoad(CheckMode).url
      )
  }

  def eabServicesProvided: Option[AnswerRow] = userAnswers.get(EabServicesProvidedPage) map {
    x =>
      if(x.size == 1) {
        AnswerRow(
          HtmlFormat.escape(messages("eabServicesProvided.checkYourAnswersLabel")),
          HtmlFormat.escape(messages(s"eabServicesProvided.${x.head}")),
          routes.EabServicesProvidedController.onPageLoad(CheckMode).url
        )
      } else {
        AnswerRow(
          HtmlFormat.escape(messages("eabServicesProvided.checkYourAnswersLabel")),
          Html("<ul class=\"list list-bullet\">" + x.map(value => "<li>" + HtmlFormat.escape(messages(s"eabServicesProvided.$value")).toString + "</li>").mkString + "</ul>"),
          routes.EabServicesProvidedController.onPageLoad(CheckMode).url
        )
      }
  }

  private def yesOrNo(answer: Boolean)(implicit messages: Messages): Html =
    if (answer) {
      HtmlFormat.escape(messages("site.yes"))
    } else {
      HtmlFormat.escape(messages("site.no"))
    }
}
