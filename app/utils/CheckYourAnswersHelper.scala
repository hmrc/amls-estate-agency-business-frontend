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

import scala.reflect.internal.util.NoSourceFile.content

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

    def clientMoneyProtectionScheme: Option[SummaryListRow] = userAnswers.get(ClientMoneyProtectionSchemePage) map {
      x =>
        SummaryListRow(
          key = Key(Text(messages("clientMoneyProtectionScheme.checkYourAnswersLabel"))),
          value = Value(yesOrNo(x)),
          actions = withChangeLink(routes.ClientMoneyProtectionSchemeController.onPageLoad(CheckMode).url)
        )
    }

    def redressScheme: Option[SummaryListRow] = userAnswers.get(RedressSchemePage) map {
      x =>
        SummaryListRow(
          key = Key(Text(messages("redressScheme.checkYourAnswersLabel"))),
          value = Value(Text(messages(s"redressScheme.$x"))),
          actions = Some(Actions(items = Seq(
            ActionItem(
              href = routes.RedressSchemeController.onPageLoad(CheckMode).url,
              content = Text(messages("site.edit"))
            )
          )))
        )
    }

    def penalisedProfessionalBodyDetail: Option[SummaryListRow] = userAnswers.get(PenalisedProfessionalBodyDetailPage) map {
      x =>
        SummaryListRow(
          key = Key(Text(messages("penalisedProfessionalBodyDetail.checkYourAnswersLabel"))),
          value = Value(Text(x)),
          actions = Some(Actions(items = Seq(
            ActionItem(
              href = routes.PenalisedProfessionalBodyDetailController.onPageLoad(CheckMode).url,
              content = Text(messages("site.edit")))
          )
          )))
    }

    def penalisedProfessionalBody: Option[SummaryListRow] = userAnswers.get(PenalisedProfessionalBodyPage) map {
      x =>
        SummaryListRow(
          key = Key(Text(messages("penalisedProfessionalBody.checkYourAnswersLabel"))),
          value = Value(yesOrNo(x)),
          actions = Some(Actions(items = Seq(
            ActionItem(
              href = routes.PenalisedProfessionalBodyController.onPageLoad(CheckMode).url,
              content = Text(messages("site.edit")))
          )
          )))
    }

    def penalisedEstateAgentsActDetail: Option[SummaryListRow] = userAnswers.get(PenalisedEstateAgentsActDetailPage) map {
      x =>
        SummaryListRow(
          key = Key(Text(messages("penalisedEstateAgentsActDetail.checkYourAnswersLabel"))),
          value = Value(Text(x)),
          actions = Some(Actions(items = Seq(
            ActionItem(
              href = routes.PenalisedEstateAgentsActDetailController.onPageLoad(CheckMode).url,
              content = Text(messages("site.edit"))
            ))
          )))
    }

    def penalisedEstateAgentsAct: Option[SummaryListRow] = userAnswers.get(PenalisedEstateAgentsActPage) map {
      x =>
        SummaryListRow(
          key = Key(Text(messages("penalisedEstateAgentsAct.checkYourAnswersLabel"))),
          value = Value(yesOrNo(x)),
          actions = Some(Actions(items = Seq(
            ActionItem(
              href = routes.PenalisedEstateAgentsActController.onPageLoad(CheckMode).url,
              content = Text(messages("site.edit"))
            ))
          )))
    }

  def eabServicesProvided: Option[SummaryListRow] = userAnswers.get(EabServicesProvidedPage) map {
    x =>
      if (x.size == 1) {
        SummaryListRow(
          key = Key(Text(messages("eabServicesProvided.checkYourAnswersLabel"))),
          value = Value(Text(messages(s"eabServicesProvided.${x.head}"))),
          actions = Some(Actions(items = Seq(
            ActionItem(
              href = routes.EabServicesProvidedController.onPageLoad(CheckMode).url,
              content = Text(messages("site.edit")))
          ))
          )
        )
      } else {
        SummaryListRow(
          key = Key(Text(messages("eabServicesProvided.checkYourAnswersLabel"))),
          value = Value(HtmlContent("<ul class=\"govuk-list govuk-list--bullet\">" + x.map(value => "<li>" + messages(s"eabServicesProvided.$value") + "</li>").mkString + "</ul>")),
          actions = Some(Actions(items = Seq(
            ActionItem(
              href = routes.EabServicesProvidedController.onPageLoad(CheckMode).url,
              content = Text(messages("site.edit")))
          ))
          )
        )

      }
  }

    private def yesOrNo(answer: Boolean)(implicit messages: Messages): Content =
      if (answer) {
        Text(messages("site.yes"))
      } else {
        Text(messages("site.no"))
      }


  private def withChangeLink(url: String) =
    Some(
      Actions(
        items =
          Seq(
            ActionItem(
              href = url,
              content = Text(messages("site.edit"))
            )
          )
      )
    )
}
