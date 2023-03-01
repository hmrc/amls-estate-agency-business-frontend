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

package models

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem

sealed trait EabServicesProvided

object EabServicesProvided extends Enumerable.Implicits {

  case object AssetManagement extends WithName("assetManagement") with EabServicesProvided
  case object Auctioneering extends WithName("auctioneering") with EabServicesProvided
  case object BusinessTransfer extends WithName("businessTransfer") with EabServicesProvided
  case object Commercial extends WithName("commercial") with EabServicesProvided
  case object DevelopmentCompany extends WithName("developmentCompany") with EabServicesProvided
  case object LandManagement extends WithName("landManagement") with EabServicesProvided
  case object Lettings extends WithName("lettings") with EabServicesProvided
  case object Relocation extends WithName("relocation") with EabServicesProvided
  case object Residential extends WithName("residential") with EabServicesProvided
  case object SocialHousingProvision extends WithName("socialHousingProvision") with EabServicesProvided

  def values: Seq[EabServicesProvided] = Seq(
    AssetManagement,
    Auctioneering,
    BusinessTransfer,
    Commercial,
    DevelopmentCompany,
    LandManagement,
    Lettings,
    Relocation,
    Residential,
    SocialHousingProvision
  )

  def options(implicit messages: Messages): Seq[CheckboxItem] = values.zipWithIndex.map {
    case (serviceValues, index) =>
      CheckboxItem(
        content = Text(messages(s"eabServicesProvided.${serviceValues.toString}")),
        value = serviceValues.toString,
        id = Some(s"value_$index"),
        name = Some(s"value[$index]")
      )
  }

      implicit val enumerable: Enumerable[EabServicesProvided] =
        Enumerable(values.map(v => v.toString -> v): _*)
  }

