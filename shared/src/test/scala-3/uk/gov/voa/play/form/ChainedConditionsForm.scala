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

import ConditionalMappings._
import play.api.data.Form
import play.api.data.Forms._

object ChainedConditionsForm {

  lazy val form = Form(mapping(
    "name" -> nonEmptyText,
    "age" -> number,
    "favouriteColour" -> mandatoryIf(
      isEqual("name", "Francoise") and isEqual("age", "21"),
      nonEmptyText
    )
  )(Model.apply)(o => Some(Tuple.fromProductTyped(o))))

  final case class Model(name: String, age: Int, favouriteColour: Option[String])
}