# play-conditional-form-mapping

Play framework conditional form mapping with field-level errors.

Play Framework will not let you assign an error message to a specific form field if you need to validate one
field based on the value of another. This micro-library provides a small collection of conditional
mappings that solve this problem.

## The Problem With Play Framework's Default Mappings

Consider the following model which represents fields on a form:

```scala
case class Model(nonUkResident: Boolean, country: Option[String], email: String)
```

Assuming this model has the validation rules:
* an ```email``` address is always mandatory
* A value for ```country``` is mandatory **IF** ```nonUkResident``` is ```true```

A typical solution following the [official play form guidelines](https://www.playframework.com/documentation/2.3.x/ScalaForms)
would look like the following:
```scala
Form(mapping(
  "nonUkResident" -> boolean,
  "country" -> optional(nonEmptyText),
  "email" -> nonEmptyText
)(Model.apply)(Model.unapply).verifying("Error.countryRequired", x => x.nonUkResident && x.country.isDefined))
```

There are two problems with this approach that have seriously negative implications on user experience:

1. **Users will have to endure two rounds of validation**. The conditional validation on ```country```
is only applied if there are no field level errors. So if there is an ```email``` validation error, the user will
first fix that, and then be told they need to supply a country.

2. **Conditional validations are always global errors**. It is not possible for the conditional error to be
assigned to the ```country``` field, so it is not easily (without overly complex tinkering) to direct
users to the field where they need to take action

The following test cases demonstrate these problems:
```scala
 behavior of "vanilla play conditional mapping"

 it should "not contain an error for the conditional validation when there is a field-level error" in {
   val data = Map("nonUkResident" -> "true")
   val res = form.bind(data)

   assert(res.errors.length == 1)
   assert(res.errors.head.key === "email")
 }

 it should "not allow a field-level error message for a conditional validation" in {
   val data = Map("nonUkResident" -> "true", "email" -> "abc@gov.uk")
   val res = form.bind(data)

   assert(res.errors.length == 1)
   assert(res.errors.head.key === "")
 }
```

## Conditional Mappings
The conditional mappings in this library remedy both of the above problems. You can validate a field based on
the value of another field, and the error will be assigned to the field that requires the attention of the
user.

This library solves these problems with a mini-dsl shown in the examples below. You can see many more examples
in this repository's unit tests.

### Example 1 - Field Becomes Mandatory If Other Boolean Field Is True
```scala
behavior of "conditional mappings"

it should "contain field level errors for fields using conditional mappings" in {
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
```

### Example 2 - Field Becomes Mandatory If Other Field Matches A Certain Value
```scala
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
```

### Example 3 - Chained Conditions
```scala
import ConditionalMappings._

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

lazy val form = Form(mapping(
  "name" -> nonEmptyText,
  "age" -> number,
  "favouriteColour" -> mandatoryIf(
    isEqual("name", "Francoise") and isEqual("age", "21"),
    nonEmptyText
  )
)(Model.apply)(Model.unapply))

case class Model(name: String, age: Int, favouriteColour: Option[String])
```

###Example 4 - Non Mandatory Conditional Mappings
If you want a mapping to apply based on the value of another field, without the target
field becoming mandatory i.e you want the form value to be ignored, you can use
the ```onlyIf``` mappings:

```scala
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
```
### Installing

Include the following dependency in your SBT build

```scala
resolvers += "HMRC-open-artefacts-maven" at "https://open.artefacts.tax.service.gov.uk/maven2"

libraryDependencies += "uk.gov.hmrc" %% "play-conditional-form-mapping" % "[INSERT_VERSION]"
```

## License ##
 
This code is open source software licensed under the [Apache 2.0 License]("http://www.apache.org/licenses/LICENSE-2.0.html"). 
