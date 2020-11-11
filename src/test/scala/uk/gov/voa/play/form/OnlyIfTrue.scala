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
import org.scalatest.OptionValues._
import play.api.data.Form
import play.api.data.Forms._

class OnlyIfTrue extends FlatSpec with Matchers {
  import ConditionalMappings._

  behavior of "isTrue and isFalse"

  it should "apply a mapping with value TRUE is string in a case-variation of TRUE" in {
    isTrue("source")(Map("source" -> "true")) should be(true)
    isTrue("source")(Map("source" -> "TRUE")) should be(true)
    isTrue("source")(Map("source" -> "TRuE")) should be(true)
  }

  it should "apply a mapping with value FALSE is string in a case-variation of FALSE" in {
    isTrue("source")(Map("source" -> "false")) should be(false)
    isTrue("source")(Map("source" -> "FALSE")) should be(false)
    isTrue("source")(Map("source" -> "fAlSe")) should be(false)
  }

  it should "apply a mapping with value TRUE is string in a case-variation of FALSE" in {
    isFalse("source")(Map("source" -> "false")) should be(true)
    isFalse("source")(Map("source" -> "FALSE")) should be(true)
    isFalse("source")(Map("source" -> "fAlSe")) should be(true)
  }

  it should "apply a mapping with value FALSE is string in a case-variation of TRUE" in {
    isFalse("source")(Map("source" -> "true")) should be(false)
    isFalse("source")(Map("source" -> "TRUE")) should be(false)
    isFalse("source")(Map("source" -> "TRuE")) should be(false)
  }

  it should "apply a mapping with value FALSE is string in not a case-variation of TRUE or FALSE" in {
    isTrue("source")(Map("source" -> "non-sensical")) should be(false)
    isFalse("source")(Map("source" -> "non-sensical")) should be(false)
  }

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
