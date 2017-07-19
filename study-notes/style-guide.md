indent with *2* spaces
long lines should be wrapped like so:

```scala
val myLongFieldNameWithNoRealPoint =
  foo(
    someVeryLongFieldName,
    andAnotherVeryLongFieldName,
    "this is a string",
    3.1415)
```

```scala
// wrong!
val myLongFieldNameWithNoRealPoint = foo(someVeryLongFieldName,
                                         andAnotherVeryLongFieldName,
                                         "this is a string",
                                         3.1415)
```

It is occasionally necessary to fully-qualify imports using `_root_`. For example if another `net` is in scope, then to access `net.liftweb` we must write `import _root_.net.liftweb._`

Scala attaches significance to whether or not a method is declared with parentheses (only applicable to methods of arity-0).

```scala
def foo1() = ...
def foo2 = ...
```
While `foo1` can be called with or without the parentheses, `foo2` **may not** be called with parentheses.


If a type parameter has a more specific meaning, a descriptive name should be used, following the class naming conventions (as opposed to an all-uppercase style): `class Map[Key, Value]`

Do not put input parameter types into parentheses if only on parameter (arity 1)

```scala
def foo(f: Int => String) = ...
```

Parentheses also serve to disable semicolon inference, and so allow the developer to start lines with operators, which some prefer:

```scala
(  someCondition
|| someOtherCondition
|| thirdCondition
)
```

### Control Structures

 - **if** - Omit braces if you have an `else` clause. Otherwise, surround the contents with curly braces even if the contents are only a single line.
 - **while** - Never omit braces (while cannot be used in a pure-functional manner).
 - **for** - Omit braces if you have a `yield` clause. Otherwise, surround the contents with curly-braces, even if the contents are only a single line.
 - **case** - Always omit braces in `case` clauses.

### For-comprehensions:

**with yield**: curly braces and each generator on its own line (if more than one)
**without yield**: chain generators with semicolons inside round brackets

```scala
for (x <- board.rows; y <- board.files) {
  printf("(%d, %d)", x, y)
}
```

this would be **wrong** with the `yield`:

```scala
for (x <- board.rows; y <- board.files)
  yield (x, y)
```

## Classes

Class/Object/Trait constructors should be declared _all on one line_, unless the line becomes “too long” (about 100 characters). In that case, put each constructor argument on its own line, indented **four** spaces and two spaces for extensions; this provides visual separation between constructor arguments and extensions:

```scala
class Person(
    name: String,
    age: Int,
    birthdate: Date,
    astrologicalSign: String,
    shoeSize: Int,
    favoriteColor: java.awt.Color)
  extends Entity
  with Logging
  with Identifiable
  with Serializable {
}
```

## Methods

Methods should be declared according to the following pattern:

```scala
def foo(bar: Baz): Bin = expr
```

_don't use procedure syntax_ (no return type, no equals, just braces after parameter list)

Methods which contain a single `match` expression should be declared in the following way:

```scala
// right!
def sum(ls: List[Int]): Int = ls match {
  case hd :: tail => hd + sum(tail)
  case Nil => 0
}

// wrong!
def sum(ls: List[Int]): Int = {
  ls match {
    case hd :: tail => hd + sum(tail)
    case Nil => 0
  }
}
```

Multiple Parameter Lists

Align the open-brackets of the parameter lists, one **list** per line (i.e. if you can’t put them _all_ on one line, put one on its own line)

Function values, method 1 and 4 are preferred:

```scala
val f1 = ((a: Int, b: Int) => a + b)
val f2 = (a: Int, b: Int) => a + b
val f3 = (_: Int) + (_: Int)
val f4: (Int, Int) => Int = (_ + _)
```

Parameters should be on the same line as the opening brace, as should the “arrow” (=>):

```scala
val f1 = { (a: Int, b: Int) =>
  val sum = a + b
  sum
}
```

