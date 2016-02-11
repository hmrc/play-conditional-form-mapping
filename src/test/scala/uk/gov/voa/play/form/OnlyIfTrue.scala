/*
 * Copyright 2016 HM Revenue & Customs
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
import org.scalatest.OptionValues._
import play.api.data.Form
import play.api.data.Forms._

class OnlyIfTrue extends FlatSpec with Matchers {
  import ConditionalMappings._

  behavior of "only if true"

  it should "apply the mapping to the target field if the source field is true" in {
    val data = Map("source" -> "true", "target" -> "Bonjour")
    val res = form.bind(data)

    assert(res.value.value === Model(true, Some("Bonjour")))
  }

  it should "ignore the mapping and set the default value if the source field is not true" in {
    val data = Map("source" -> "false", "target" -> "Bonjour")
    val res = form.bind(data)

    assert(res.value.value === Model(false, None))
  }

  it should "not mandate the target field even if the source field is true" in {
    val data = Map("source" -> "true")
    val res = form.bind(data)

    assert(res.errors.isEmpty)
  }

  lazy val form = Form(mapping(
    "source" -> boolean,
    "target" -> onlyIfTrue("source", optional(nonEmptyText))
  )(Model.apply)(Model.unapply))

  case class Model(source: Boolean, target: Option[String])
}
