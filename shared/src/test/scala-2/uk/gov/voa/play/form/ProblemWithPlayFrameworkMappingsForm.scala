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

object ProblemWithPlayFrameworkMappingsForm {

  lazy val form = Form(mapping(
    "nonUkResident" -> boolean,
    "country" -> optional(nonEmptyText),
    "email" -> nonEmptyText
  )(Model.apply)(Model.unapply).verifying("Error.countryRequired", x => x.nonUkResident && x.country.isDefined))

  final case class Model(nonUkResident: Boolean, country: Option[String], email: String)
}

object SolutionUsingConditionalMappingsForm {

  lazy val form = Form(mapping(
    "nonUkResident" -> boolean,
    "country" -> mandatoryIfTrue("nonUkResident", nonEmptyText),
    "email" -> nonEmptyText
  )(Model.apply)(Model.unapply))

  final case class Model(nonUkResident: Boolean, country: Option[String], email: String)
}
