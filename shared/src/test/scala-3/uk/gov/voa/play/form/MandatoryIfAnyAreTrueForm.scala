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

object MandatoryIfAnyAreTrueForm {

  lazy val form = Form(mapping(
    "f1" -> boolean,
    "f2" -> boolean,
    "f3" -> boolean,
    "target" -> mandatoryIfAnyAreTrue(Seq("f1", "f2", "f3"), nonEmptyText)
  )(Model.apply)(o => Some(Tuple.fromProductTyped(o))))

  final case class Model(f1: Boolean, f2: Boolean, f3: Boolean, target: Option[String])
}
