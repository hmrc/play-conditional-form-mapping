/*
 * Copyright 2020 HM Revenue & Customs
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

import org.scalatest.{Matchers, FlatSpec}
import play.api.data.Form
import play.api.data.Forms._

class ProblemWithPlayFrameworkMappings extends FlatSpec with Matchers {

  behavior of "vanilla play conditional mapping"

  it should "not contain an error for the conditional validation when there is a field-level error" in {
    val data = Map("nonUkResident" -> "true")
    val res = form.bind(data)

    assert(res.errors.length == 1)
    assert(res.errors.head.key === "email")
  }

  it should "not allow an field-level error message for a conditional validation" in {
    val data = Map("nonUkResident" -> "true", "email" -> "abc@gov.uk")
    val res = form.bind(data)

    assert(res.errors.length == 1)
    assert(res.errors.head.key === "")
  }

  lazy val form = Form(mapping(
    "nonUkResident" -> boolean,
    "country" -> optional(nonEmptyText),
    "email" -> nonEmptyText
  )(Model.apply)(Model.unapply).verifying("Error.countryRequired", x => x.nonUkResident && x.country.isDefined))

}

case class Model(nonUkResident: Boolean, country: Option[String], email: String)

class SolutionUsingConditionalMappings extends FlatSpec with Matchers {
  import ConditionalMappings._

  behavior of "conditional mappings"

  it should "contain a field level errors for the field and conditional mappings" in {
    val data = Map("nonUkResident" -> "true")
    val res = form.bind(data)

    assert(res.errors.length == 2)
    assert(res.errors.head.key === "country")
    assert(res.errors.tail.head.key === "email")
  }

  lazy val form = Form(mapping(
    "nonUkResident" -> boolean,
    "country" -> mandatoryIfTrue("nonUkResident", nonEmptyText),
    "email" -> nonEmptyText
  )(Model.apply)(Model.unapply))
}
