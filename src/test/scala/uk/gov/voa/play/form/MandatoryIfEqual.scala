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

package uk.gov.voa.play.form

import org.scalatest.{Matchers, FlatSpec}
import play.api.data.Form
import play.api.data.Forms._

class MandatoryIfEqual extends FlatSpec with Matchers {
  import ConditionalMappings._

  behavior of "mandatory if equal"

  it should "mandate the target field if the source has the required value" in {
    val data = Map("country" -> "England")
    val res = form.bind(data)

    assert(res.errors.head.key === "town")
  }

  it should "not mandate the target field if the source field does not have the required value" in {
    val data = Map("country" -> "Scotland")
    val res = form.bind(data)

    assert(res.errors.isEmpty)
  }

  lazy val form = Form(mapping(
    "country" -> nonEmptyText,
    "town" -> mandatoryIfEqual("country", "England", nonEmptyText)
  )(Model.apply)(Model.unapply))

  case class Model(country: String, town: Option[String])
}
