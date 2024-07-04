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

package uk.gov.voa.play.form

import play.api.data.Form
import play.api.data.Forms._
import ConditionalMappings._

object OnlyIfAnyForm {
  
  lazy val form = Form(mapping(
    "s1" -> nonEmptyText,
    "s2" -> nonEmptyText,
    "s3" -> nonEmptyText,
    "target" -> onlyIfAny(Seq("s1" -> "magicValue", "s2" -> "magicValue", "s3" -> "magicValue"), optional(nonEmptyText))
  )(Model.apply)(Model.unapply))

  case class Model(s1: String, s2: String, s3: String, target: Option[String])
}
