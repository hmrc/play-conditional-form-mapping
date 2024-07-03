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

import org.scalatest.OptionValues._
import org.scalatest.flatspec.AnyFlatSpecLike
import org.scalatest.matchers.should.Matchers
import OnlyIfAnyForm._

class OnlyIfAny extends AnyFlatSpecLike with Matchers {

  behavior of "only if any"

  it should "apply the mapping to the target field if any of the source fields have their required value" in {
    val data = Map("s1" -> "abc", "s2" -> "abc", "s3" -> "abc", "target" -> "magic")
    Seq("s1", "s2", "s3") foreach { f =>
      val d = data.updated(f, "magicValue")
      val res = form.bind(d)

      assert(res.value.value.target.value === "magic")
    }
  }

  it should "not apply the mapping to the target field neither of the source fields have the required value" in {
    val data = Map("s1" -> "abc", "s2" -> "abc", "s3" -> "abc", "target" -> "magic")
    val res = form.bind(data)

    assert(res.value.value.target === None)
  }
}
