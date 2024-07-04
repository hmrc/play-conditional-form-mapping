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
import ChainedConditionsForm._

class ChainedConditions extends AnyFlatSpecLike with Matchers {

  behavior of "chained mappings"

  it should "apply mappings if all of the chained criteria are satisfied" in {
    val data = Map("name" -> "Francoise", "age" -> "21")
    val res = form.bind(data)

    assert(res.errors.head.key === "favouriteColour")
  }

  it should "not apply mappings if any part of the chained critieria is not satisfied" in {
    val data = Map("name" -> "Francoise", "age" -> "20")
    val res = form.bind(data)

    assert(res.errors.isEmpty)
  }
}
