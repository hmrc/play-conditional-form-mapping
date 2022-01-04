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

import play.api.data.{FormError, Mapping}
import play.api.data.validation.Constraint


case class ConditionalMapping[T](condition: Condition, wrapped: Mapping[T], defaultValue: T,
  constraints: Seq[Constraint[T]] = Nil, keys: Set[String] = Set()) extends Mapping[T] {

  override val format: Option[(String, Seq[Any])] = wrapped.format

  val key = wrapped.key

  def verifying(addConstraints: Constraint[T]*): Mapping[T] =
    this.copy(constraints = constraints ++ addConstraints.toSeq)

  def bind(data: Map[String, String]): Either[Seq[FormError], T] =
    if (condition(data)) wrapped.bind(data) else Right(defaultValue)

  def unbind(value: T): Map[String, String] = wrapped.unbind(value)

  def unbindAndValidate(value: T): (Map[String, String], Seq[FormError]) = wrapped.unbindAndValidate(value)

  def withPrefix(prefix: String): Mapping[T] = copy(wrapped = wrapped.withPrefix(prefix))

  val mappings: Seq[Mapping[_]] = wrapped.mappings :+ this
}
