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

package pages

import models.EabServicesProvided.{Lettings, Residential}
import models.{EabServicesProvided, UserAnswers}
import play.api.libs.json.JsPath

import scala.util.Try

case object EabServicesProvidedPage extends QuestionPage[Seq[EabServicesProvided]] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "eabServicesProvided"

  override def cleanup(value: Option[Seq[EabServicesProvided]], userAnswers: UserAnswers): Try[UserAnswers] = {
    value map { ans =>
      val cleanupResidential = ans.contains(Residential) match {
        case true => super.cleanup(value, userAnswers)
        case _    => userAnswers.remove(RedressSchemePage)
      }

      ans.contains(Lettings) match {
        case true => super.cleanup(value, userAnswers)
        case _    => cleanupResidential.flatMap(_.remove(ClientMoneyProtectionSchemePage))
      }
    }
  }.getOrElse(super.cleanup(value, userAnswers))
}
