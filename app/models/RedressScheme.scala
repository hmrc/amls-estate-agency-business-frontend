/*
 * Copyright 2019 HM Revenue & Customs
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

import viewmodels.RadioOption

sealed trait RedressScheme

object RedressScheme extends Enumerable.Implicits {

  case object NotRegistered extends WithName("notRegistered") with RedressScheme
  case object PropertyRedressScheme extends WithName("propertyRedressScheme") with RedressScheme
  case object ThePropertyOmbudsman extends WithName("propertyOmbudsman") with RedressScheme

  val values: Seq[RedressScheme] = Seq(
    NotRegistered,
    PropertyRedressScheme,
    ThePropertyOmbudsman
  )

  val options: Seq[RadioOption] = values.map {
    value =>
      RadioOption("redressScheme", value.toString)
  }

  implicit val enumerable: Enumerable[RedressScheme] =
    Enumerable(values.map(v => v.toString -> v): _*)
}
