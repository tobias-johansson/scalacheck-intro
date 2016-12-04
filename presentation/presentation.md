
class: inverse
layout: true

---
class: center, middle

# Automated property-based testing
# with ScalaCheck

Tobias Johansson


---


# Agenda

- Introduction
	- Property-based testing
	- ScalaCheck
	- Generators
- Demo
	- Finding bugs in functional code
	- Finding bugs in a stateful code
- Discussion

---


class: center, middle

# Property-based testing

---


## Normal tests

- Define **specific** input values
- Execute code under test
- Expect **specific** output values

--

### Example

```scala
"Math.abs should be positive" in {

	Math.abs(-1) shouldBe 1

}
```

???
- Using ScalaTest
- input: -1, CUT: Math.abs, output: 1

--

### Abstract

*Describes one example of what the code under test does*

---


## Property tests

- Define input value **domains**
- Define **properties** that should hold
- Validate properties using generated input data

???
**domains** or **ranges** or **sets**

--

### Example

```scala
"Math.abs should always be positive" in check {

	x: Int =>

		Math.abs(x) >= 0

}
```
???
- Using ScalaCheck in ScalaTest
- System will attempt to falsify property
- Property in test will be evaluated many times over
	- Some evaluation false -> test failure
	- No evaluation false -> test success

--

### Abstract

*Describes a property of the code under test*

---

class: center, middle

# ScalaCheck

---

## ScalaCheck

--

- Stems from
  - QuickCheck (haskell, erlang)
- Implemented in many languages

--

## Used here
```scala
libraryDependencies += "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
```
--
```scala
libraryDependencies += "org.scalatest"  %% "scalatest"  % "3.0.0"  % "test"
```

---


## Properties
```scala
"Some test" in check { (a: List[Int], b: List[Int]) =>

	(a ++ b).size == a.size + b.size

}
```

???
- `check` is ScalaTest syntax

--
is a short form of
```scala
"Some test" in check {

	forAll { (a: List[Int], b: List[Int]) =>

		(a ++ b).size == a.size + b.size

	}
}
```
???
- `forAll` is ScalaCheck syntax
- `forAll` is a central function

---


Pseudo signature
```scala
def forAll [T: Arbitrary] (f: T => Boolean): Prop
```
???
- Pseudo signature of `forAll`
- Works for any `T: Arbitrary` (has implicit instance)
--
Predefined instances
```scala
Arbitrary[Int]
Arbitrary[String]
Arbitrary[java.util.Date]

...

Arbitrary[Option[T]]
Arbitrary[List[T]]

...

```
???
- Predefined instances of Arbitrary
- **Demo** in REPL
```scala
forAll { i: Int => true }
forAll { i: Int => println(i); true } check
forAll { i: List[Double] => println(i); true } check
```

---


## Generators
???
- Arbitrary instances are value **generators**
- Generators define the **domains** for our properties
- We can create our own Generators!
--

```scala
val letters = Gen.oneOf('A', 'B', 'C', 'D', 'E', 'F')
```
--
Used in property
```scala
"A prop" in check {
	forAll(letters) { c: Char => ??? }
}
```
???
- We want to verify something over these chars
--
Pseudo signature
```scala
def forAll [T] (g: Gen[T]) (f: T => Boolean): Prop
```
???
- Arbitrary is basically just a special name for certain Generators

---

## Composing generators
???
`Gen` contains lots of factories/combinators

--

```scala
val letters    = Gen.oneOf('A', 'B', 'C', 'D', 'E', 'F')
val digits     = Gen.choose('0', '9')
val hexChars   = Gen.oneOf(letters, digits)
val hexStrings = Gen.listOf(hexChars).map(_.mkString)
```

--
Used in property
```scala
"A prop" in check {
	forAll(hexStrings) { s: String => ??? }
}
```

---

## Composing generators

```scala
case class Address(street: String, number: Int)
case class Person(name: String, age: Int, address: Address)
```
--

```scala
val addresses = for {
  street <- Gen.alphaStr
  number <- Gen.posNum[Int]
} yield Address(street, number)

val persons = for {
  name    <- Gen.alphaStr
  age     <- Gen.choose(0, 120)
  address <- addresses
} yield Person(name, age, address)
```

---

class: center, middle

# Demo

???
- Implement MyJsonTest
  - Increase minSuccessful
	- Discuss Shrinking
- Implement ListBufferTest
- Implement StorageServiceTest

---

## Discussion

### Non-determinism (flaky tests)
- A failure is a failure, and should be handled
--
- Failing inputs are logged -> create "example tests"
- Possible to set the rng seed (don't know how; not well documented it seems)

---

## Discussion

- How to know when to use it?
  - Trade-off: A bit harder to define property tests instead of example tests
    - Find simple properties
    - Probably easiest for library-like code
    - Can create constant/choice generators if too for hard to generate certain parts
	- Many projects have a combination of "example tests" and "property tests"
