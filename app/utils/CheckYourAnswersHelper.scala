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

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages._
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.{Actions, HtmlContent, Key, Value}
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.{Content, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.{ActionItem, SummaryListRow}

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  def getAllRows: Seq[SummaryListRow] = Seq(
    eabServicesProvided,
    redressScheme,
    clientMoneyProtectionScheme,
    penalisedEstateAgentsAct,
    penalisedEstateAgentsActDetail,
    penalisedProfessionalBody,
    penalisedProfessionalBodyDetail
  ).flatten

  def clientMoneyProtectionScheme: Option[SummaryListRow] = userAnswers.get(ClientMoneyProtectionSchemePage) map { x =>
    val msg = "clientMoneyProtectionScheme.checkYourAnswersLabel"
    SummaryListRow(
      key = Key(Text(messages(msg))),
      value = Value(yesOrNo(x)),
      actions = withChangeLink(
        routes.ClientMoneyProtectionSchemeController.onPageLoad(CheckMode).url,
        msg
      )
    )
  }

  def redressScheme: Option[SummaryListRow] = userAnswers.get(RedressSchemePage) map { x =>
    val msg = "redressScheme.checkYourAnswersLabel"
    SummaryListRow(
      key = Key(Text(messages(msg))),
      value = Value(Text(messages(s"redressScheme.$x"))),
      actions = withChangeLink(
        routes.RedressSchemeController.onPageLoad(CheckMode).url,
        msg
      )
    )
  }

  def penalisedProfessionalBodyDetail: Option[SummaryListRow] =
    userAnswers.get(PenalisedProfessionalBodyDetailPage) map { x =>
      val msg = "penalisedProfessionalBodyDetail.checkYourAnswersLabel"
      SummaryListRow(
        key = Key(Text(messages(msg))),
        value = Value(Text(x)),
        actions = withChangeLink(
          routes.PenalisedProfessionalBodyDetailController.onPageLoad(CheckMode).url,
          msg
        )
      )
    }

  def penalisedProfessionalBody: Option[SummaryListRow] = userAnswers.get(PenalisedProfessionalBodyPage) map { x =>
    val msg = "penalisedProfessionalBody.checkYourAnswersLabel"
    SummaryListRow(
      key = Key(Text(messages(msg))),
      value = Value(yesOrNo(x)),
      actions = withChangeLink(
        routes.PenalisedProfessionalBodyController.onPageLoad(CheckMode).url,
        msg
      )
    )
  }

  def penalisedEstateAgentsActDetail: Option[SummaryListRow] = userAnswers.get(PenalisedEstateAgentsActDetailPage) map {
    x =>
      val msg = "penalisedEstateAgentsActDetail.checkYourAnswersLabel"
      SummaryListRow(
        key = Key(Text(messages(msg))),
        value = Value(Text(x)),
        actions = withChangeLink(
          routes.PenalisedEstateAgentsActDetailController.onPageLoad(CheckMode).url,
          msg
        )
      )
  }

  def penalisedEstateAgentsAct: Option[SummaryListRow] = userAnswers.get(PenalisedEstateAgentsActPage) map { x =>
    val msg = "penalisedEstateAgentsAct.checkYourAnswersLabel"
    SummaryListRow(
      key = Key(Text(messages(msg))),
      value = Value(yesOrNo(x)),
      actions = withChangeLink(
        routes.PenalisedEstateAgentsActController.onPageLoad(CheckMode).url,
        msg
      )
    )
  }

  def eabServicesProvided: Option[SummaryListRow] = userAnswers.get(EabServicesProvidedPage) map { x =>
    val valueRow = if (x.size == 1) {
      Value(Text(messages(s"eabServicesProvided.${x.head}")))
    } else {
      Value(
        HtmlContent(
          "<ul class=\"govuk-list govuk-list--bullet\">" + x
            .map(value => "<li>" + messages(s"eabServicesProvided.$value") + "</li>")
            .mkString + "</ul>"
        )
      )
    }

    val msg = "eabServicesProvided.checkYourAnswersLabel"

    SummaryListRow(
      key = Key(Text(messages(msg))),
      value = valueRow,
      actions = withChangeLink(
        routes.EabServicesProvidedController.onPageLoad(CheckMode).url,
        msg
      )
    )
  }

  private def yesOrNo(answer: Boolean)(implicit messages: Messages): Content =
    if (answer) {
      Text(messages("site.yes"))
    } else {
      Text(messages("site.no"))
    }

  private def withChangeLink(url: String, hiddenTextKey: String) =
    Some(
      Actions(
        items = Seq(
          ActionItem(
            href = url,
            content = Text(messages("site.edit")),
            visuallyHiddenText = Some(messages(s"$hiddenTextKey.hidden"))
          )
        )
      )
    )
}
