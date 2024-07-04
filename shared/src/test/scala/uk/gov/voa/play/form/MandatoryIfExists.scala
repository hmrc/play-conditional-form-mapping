/*
 * Copyright 2022 HM Revenue & Customs
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

import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import MandatoryIfExistsForm._

class MandatoryIfExists extends AnyFlatSpecLike with Matchers {

  behavior of "mandatory if exists"

  it should "mandate the target field if any value for the source field is supplied" in {
    val data = Map("source" -> "anyrandomvalue")
    val res = form.bind(data)

    assert(res.errors.head.key === "target")
  }

  it should "not mandate the target field is  no value for the source field is supplied" in {
    val res = form.bind(Map.empty[String, String])

    assert(res.errors.isEmpty)
  }
}
