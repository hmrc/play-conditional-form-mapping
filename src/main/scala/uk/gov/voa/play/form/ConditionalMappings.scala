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

import play.api.data.Mapping

import scala.util.Try

object ConditionalMappings {
  def isTrue(field: String): Condition = _.get(field).flatMap(v => Try(v.toBoolean).toOption).getOrElse(false)

  def isFalse(field: String): Condition = _.get(field).flatMap(v => Try(!v.toBoolean).toOption).getOrElse(false)

  def isEqual(field: String, value: String): Condition = _.get(field).map(_ == value).getOrElse(false)

  def isNotEqual(field: String, value: String): Condition = _.get(field).map(_ != value).getOrElse(false)

  def isAnyOf(field: String, values: Seq[String]): Condition = _.get(field).map(values.contains).getOrElse(false)

  def isNotAnyOf(field: String, values: Seq[String]): Condition = _.get(field).map(values.contains).exists(_ == false)

  def onlyIf[T](c: Condition, mapping: Mapping[T])(implicit nonMapValue: T): Mapping[T] =
    ConditionalMapping(c, mapping, nonMapValue)

  def onlyIfTrue[T](fieldName: String, mapping: Mapping[T])(implicit nonMapValue: T): Mapping[T] =
    onlyIf(isTrue(fieldName), mapping)

  def onlyIfAny[T](pairs: Seq[(String, String)], mapping: Mapping[T])(implicit nonMapValue: T): Mapping[T] = {
    val allowable: Set[(String, String)] = pairs.toSet
    def condition(data: Map[String, String]) = allowable.intersect(data.toSet).nonEmpty
    ConditionalMapping(condition, mapping, nonMapValue, keys = pairs.toMap.keySet)
  }

  def mandatoryIf[T](condition: Condition, mapping: Mapping[T]) =
    ConditionalMapping(condition, MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)

  def mandatoryIfTrue[T](fieldName: String, mapping: Mapping[T], prefix: Option[String] = None): Mapping[Option[T]] =
    ConditionalMapping(isTrue(fieldName)(_), MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)

  def mandatoryIfAnyAreTrue[T](fields: Seq[String], mapping: Mapping[T], prefix: Option[String] = None,
                               showNestedErrors: Boolean = true, fieldsToExclude: Seq[String] = Seq.empty): Mapping[Option[T]] = {
    ConditionalMapping(x => fields.exists(isTrue(_)(x)), MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)
  }

  def mandatoryIfFalse[T](fieldName: String, mapping: Mapping[T], prefix: Option[String] = None): Mapping[Option[T]] =
    ConditionalMapping(isFalse(fieldName), MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)

  def mandatoryBooleanIfTrue(fieldName: String, mapping: Mapping[Boolean], prefix: Option[String] = None) =
    ConditionalMapping(isTrue(fieldName), MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)

  def mandatoryIfAnyOf[T](fieldName: String, values: Seq[String], mapping: Mapping[T]): Mapping[Option[T]] =
    ConditionalMapping(isAnyOf(fieldName, values), MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)

  def mandatoryAndOnlyIfAnyOf[T](fieldName: String, values: Seq[String], mapping: Mapping[T]): Mapping[Option[T]] =
    onlyIf(isAnyOf(fieldName, values), MandatoryOptionalMapping(mapping, Nil))

  def mandatoryIfNot[T](fieldName: String, value: String, mapping: Mapping[T]): Mapping[Option[T]] =
    ConditionalMapping(isNotEqual(fieldName, value), MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)

  def mandatoryIfExists[T](fieldName: String, mapping: Mapping[T]): Mapping[Option[T]] =
    ConditionalMapping(_.keys.toSeq.contains(fieldName), MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)

  def mandatoryIfEqual[T](fieldName: String, value: String, mapping: Mapping[T]): Mapping[Option[T]] = {
    val condition: Condition = _.get(fieldName).map(_ == value).getOrElse(false)
    ConditionalMapping(condition, MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)
  }

  def mandatoryIfEqualToAny[T](fieldName: String, values: Seq[String], mapping: Mapping[T]): Mapping[Option[T]] =
    ConditionalMapping(isAnyOf(fieldName, values), MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)

  def mandatoryIfAllEqual[T](pairs: Seq[(String, String)], mapping: Mapping[T], prefix: Option[String] = None,
                             showNestedErrors: Boolean = true, error: Option[String] = None): Mapping[Option[T]] = {
    val condition: Condition = x => (for (pair <- pairs) yield x.get(pair._1).contains(pair._2)).forall(b => b)
    ConditionalMapping(condition, MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)
  }

  def mandatory[T](mapping: Mapping[T], prefix: Option[String] = None, showNestedErrors: Boolean = true) =
    ConditionalMapping(x => true, MandatoryOptionalMapping(mapping, Nil), None, Seq.empty)
}
