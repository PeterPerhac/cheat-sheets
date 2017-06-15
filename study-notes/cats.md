# Cats

## Semigroup

A semigroup for type `A` will have a `combine` operation that takes two `A`s and returns an `A`. This operation must be guaranteed to be **associative**.

**((a combine b) combine c) = (a combine (b combine c))**

```scala
import cats.Semigroup
import cats.implicits._
```

There is inline syntax available for `Semigroup`: `|+|`
there aren't type class instances for `Some` or `None`, but for `Option`.

## Monoid

`Monoid` extends the `Semigroup` type class, adding an `empty` method to semigroup's `combine`. The `empty` method must return a **value** that when combined with any other instance of that type returns the other instance, i.e.

```scala
(combine(x, empty) == combine(empty, x) == x)
```

Having an `empty` defined allows us to combine all the elements of some *potentially empty* collection of `T` for which a `Monoid[T]` is defined and return a `T`, rather than an `Option[T]` as we have a sensible default to fall back to.

Cats implementations of `Monoid` provide a `combineAll` method too:

```scala
Monoid[String].combineAll(List("a", "b", "c")) should be("abc")
```

There are some data types for which we cannot define an empty element. Cats has a `NonEmptyList` data type that has an implementation of `Semigroup` but no implementa on of `Monoid`.

## Functor

A `Functor` is a ubiquitous type class involving types that have one "hole", i.e. types which have the shape `F[_]`, such as `Option`, `List` and `Future`. The `Functor` category involves a single operation, named `map`:

```scala
def map[A, B](fa: F[A])(f: A => B): F[B]
```
Functors compose. Given any functor `F[_]` and any functor `G[_]` we can create a new functor `F[G[_]]` by composing them:

```scala
val listOpt = Functor[List] compose Functor[Option]
listOpt.map(List(Some(1), None, Some(3)))(_ + 1) should be( List(Some(2), None, Some(4)))
```

## Apply

`Apply` extends the `Functor` type class, adding method `ap`.

The `ap` function is similar to `map` in that we are transforming **a value in a context** (a *context* being the `F` in `F[A]`; a **context** can be `Option`, `List` or `Future` etc).

The difference between `ap` and `map` is that for `ap` the function that takes care of the transformation is of type `F[A => B]`, whereas for `map` it is `A => B`

`ap` maps a **unary** function in context over a value in context. `ap2`, `ap3` .. `ap22` map a binary/ternary/etc function over `n` values in a context, e.g.:

```scala
val binary = (a: Int, b: Int) ⇒  a + b
Apply[Option].ap2(Some(binary))(Some(1), Some(2)) should be(Some(3))
Apply[Option].map2(Some(1), Some(2))(binary) should be(Some(3))
```

As you can see above, there are also higher arity methods `map` and `tupled` which work as expected.

We can build `Apply`s of higher arity (n=2,3 .. 22) using the `|@|` operator (it will produce a `CarthesianBuilderN`). In order to use it, first `import cats.implicits._.` All instances created by `|@|` have `map`, `ap`, and `tupled` methods of the appropriate arity.

```scala
val option2 = Option(1) |@| Option(2)
option2 ap Some(binaryPlus) should be(Some(3))
```

## Applicative

`Applicative` extends `Apply` by adding a single method, `pure`:

```scala
def pure[A](x: A): F[A]
```

`pure` takes any **value** and returns the value **in the context of the functor**. (remember, `Applicative` *is* an `Apply`, which *is* a `Functor`).

```scala
Applicative[List] compose Applicative[Option]).pure(1) should be(List(Some(1)))
// note it's _not_ Some(List(1))
```

### Applicative Functors & Monads

`Applicative` is a generalization of `Monad`, allowing expression of effectful computations in a pure functional way. `Applicative` is generally preferred to `Monad` when the *structure* of a computation is fixed a priori. That makes it possible to perform certain kinds of static analysis on applicative values.

```scala
Monad[Option].pure(1) should be(Some(1))
Applicative[Option].pure(1) should be(Some(1))
```

## Monad

`Monad` extends the `Applicative` type class with a new function `flatten`, which takes a value in a nested context (eg. `F[F[A]]` where `F` is the context) and *joins* the contexts together so that we have a single context (ie. `F[A]`).

### Monad instances

If `Applicative` is already present and `flatten` is well-behaved, extending the `Applicative` to a `Monad` is trivial. To provide evidence that a type belongs in the `Monad` type class, cats' implementation requires us to provide an implementation of `pure` (which can be reused from `Applicative`) and `flatMap`.

We can use `flatten` to define `flatMap`: `flatMap` is just `map` followed by `flatten`. Conversely, `flatten` is just `flatMap` using the identity function `x => x` (i.e. `flatMap(_)(x => x))`.

Here's a mind bender:

```scala
Monad[List].ifM(List(true, false, true))(List(1, 2), List(3, 4)) should be(List(1, 2, 3, 4, 1, 2))
```

To understand what's going on in the example above, it's worth looking at the method signature of `ifM`: first parameter list defines `fa`, which is some `F[A]`. The second parameter list defines exactly _two_ parameters: `ifTrue` and `ifFalse` so the above example works like this: `map` the first true to `List(1,2)`, then `map` false to `List(3,4)` and `map` the last true to `List(1,2)` again, then `flatten` the result.

**`ifM` provides the ability to choose later operations in a sequence, based on the results of earlier ones.** `ifM` lifts an **if** statement into the monadic context.

Cats provides a monad transformer for `Option` called `OptionT`:

```scala
OptionT[List,Int].pure(42) shouldBe OptionT(List(Some(42)))
```

There's also additional syntax provided for creating instances of OptionT from plain values:

If you have only an A and you wish to lift it into an OptionT[F,A] assuming you have an Applicative instance for F you can use some which is an alias for pure. There also exists a none method which can be used to create an OptionT[F,A], where the Option wrapped A type is actually a None:

```scala
val greet:       OptionT[Future,String] = OptionT.pure("Hi!")
val greetAlt:    OptionT[Future,String] = OptionT.some("Hi!")
val failedGreet: OptionT[Future,String] = OptionT.none
```

The `.none` syntax is nice in that it creates the None of required type.

This is for un-nesting an `F[Option[T]]` into an `OptionT[F, T]` which is easier to use in for-comprehensions. Calling `.value` on an `OptionT` will return the nested monadic structure.

```scala
val fos: Future[Option[String]] = Future.successful(Some("foo"))
val fs: Future[String] = Future.successful("foo")
val os: Option[String] = Some("foo")

val ot: OptionT[Future, String] = for {
    s1 <- OptionT(greetingFO)
    s2 <- OptionT.liftF(firstnameF)
    s3 <- OptionT.fromOption[Future](lastnameO)
} yield s"$s1 $s2 $s3"
//ot.value will convert OptionT back to Future[Option[String]]
```

This can also be summarised like so:

```scala
OptionT.pure[F,T](T) // form T
OptionT(F[Option[T]]) // from F[O[T]]
OptionT.liftF(F[T]) // from F[T]
OptionT.fromOption[Future](Option[T]) // from O[T]
```

###mapping and flatMapping the Monad Transformer

mapping a MonadTransformer `MT[F,A]` produces `MT[F,B]` provided we have a mapping function of the shape `A => B`

if the mapping function, however, is of another shape we will need to use different mathods on the MT to do the mapping:

- `A => MT[F, B]` requires a `flatMap`
- `A => F[B]` requires a `semiflatMap`
- `A => M[B]` requires a `subflatMap` (where M is the monad of the MT transformer)


```scala
val mapFu : String => String = ???
val flatmapFu : String => OptionT[Future, String] = ???
val semiflatMapFu : String => Future[String] = ???
val subflatMapFu : String => Option[String] = ???

val otInt = OptionT[Future, Int].some(42)

otInt.map(mapFu) //OptionT[Future, String]
otInt.flatMap(flatmapFu) //OptionT[Future, String]
otInt.semiflatMap(semiflatMapFu) //OptionT[Future, String]
otInt.subflatMap(subflatMapFu) //OptionT[Future, String]

```

## CoflatMap

the `CoflatMap` type class is the _dual_ of `FlatMap`.

```scala
trait CoflatMap[F[_]] extends Functor[F]
```

It applies a value in a context to a function that takes a value in a context and returns a normal value.

```scala
def coflatMap[A, B](fa: F[A])(f: F[A] => B): F[B]
```

Whereas `flatten` removes a layer of `F`, `coflatten` adds a layer of `F`:

```scala
def coflatten[A](fa: F[A]): F[F[A]] = coflatMap(fa)(fa => fa)
```

## Foldable

Foldable type class instances can be defined for data structures that can be folded to a summary value.

Define in terms of these two *basic* methods:
 * `foldLeft` - eager left-associative
 * `foldRight` - lazy right-associative; lazy in order to be stack-safe

`fold` - also called `combineAll`, combines every value in the `Foldable` using the given `Monoid` instance.

```scala
Foldable[List].fold(List("a", "b", "c")) should be("abc")
Foldable[List].fold(List(1, 2, 3)) should be(6)
```

`foldMap` is similar to `fold` but maps every `A` value into `B` and then combines them using the given `Monoid[B]` instance:

```scala
Foldable[List].foldMap(List("a", "b", "c"))(_.length) should be(3)
Foldable[List].foldMap(List(1, 2, 3))(_.toString) should be("123")
```

Other methods on the `Foldable`: `foldK`, `find`, `exists`, `forall`, `toList`, `isEmpty`, `filter_`, `dropWhile_`, `takeWhile_`
`filter_` will convert `F[A]` to `List[A]` only including the elements that match a predicate. (similarly for other methods ending with underscore)

Composing `Foldable`s is easy and offers interesting results:

```scala
val FoldableListOption = Foldable[List].compose[Option]
FoldableListOption.fold(List(Option(1), Option(2), Option(3), Option(4))) should be(10)
FoldableListOption.fold(List(Option("1"), Option("2"), None, Option("3"))) should be("123")
```

## Traverse

`Traverse` trait extends `Foldable` and `Functor` and adds `traverse` operation.

```scala
trait Traverse[F[_]] {
  def traverse[G[_]: Applicative, A, B](fa: F[A])(f: A => G[B]): G[F[B]]
}
```

`traverse` says given a collection (or other container) of data, and a function that takes a piece of data and returns an effectful value, it will traverse the collection, applying the function and aggregating the effectful values as it goes.

```scala
import cats.Semigroup
import cats.data.{NonEmptyList, OneAnd, Validated, ValidatedNel, Xor}
import cats.implicits._

def parseIntXor(s: String): Xor[NumberFormatException, Int] =
  Xor.catchOnly[NumberFormatException](s.toInt)

List("1", "2", "3").traverseU(parseIntXor) should be(Xor.Right(List(1,2,3)))
List("1", "abc", "3").traverseU(parseIntXor).isLeft should be(true)
```

`traverseU` is for all intents and purposes the same as `traverse`, but with some type-level trickery to allow it to infer the `Applicative[Xor[A, ?]]` and `Applicative[Validated[A, ?]]` instances - *scalac* has issues inferring the instances for data types that do not trivially satisfy the `F[_]` shape required by `Applicative`.

Once `Xor` (or `Either`) hits its first bad parse, it will **not** attempt to parse any others down the line (similar behavior would be found with using `Option` as the effect). Contrast this with `Validated` where even if one bad parse is hit, it will **continue** trying to parse the others, accumulating any and all errors as it goes.

We can write an `Applicative` instance for `Future` that runs each `Future` concurrently. Then when we traverse a `List[A]` with an `A => Future[B]`, we can imagine the traversal as a scatter-gather. Each `A` creates a concurrent computation that will produce a `B` (the scatter), and as the `Future`s complete they will be gathered back into a `List`.

Having a collection of data, each of which is already in an effect (e.g. List[Option[A]]), we can use `traverse` with the identity function to make an `Option[List[A]]`. `Traverse` provides a convenience method `sequence` that does exactly this.

```scala
import cats.implicits._

List(Option(1), Option(2), Option(3)).traverse(identity) should be(Some(List(1,2,3)))
List(Option(1), None, Option(3)).traverse(identity) should be(None) //note it's none, _not_ Some(List(1,3))

//or simpler:
List(Option(1), Option(2), Option(3)).sequence
List(Option(1), None, Option(3)).sequence
```

Sometimes our effectful functions return a `Unit` value in cases where there is no interesting value to return (e.g. writing to some sort of store). Traversing solely for the sake of the effect (ignoring any values that may be produced, `Unit` or otherwise) is **common**, so `Foldable` (superclass of `Traverse`) provides `traverse_` and `sequence_` methods that do the same thing as `traverse` and `sequence` but _ignores_ any value produced along the way, returning `Unit`.


## Identity

The **identity monad** can be seen as the ambient monad that _encodes the effect of having no effect_. It is ambient in the sense that plain pure values are values of `Id`. It is encoded as:

```scala
type Id[A] = A
```

The type `Id[A]` is just an alias for `A` and allows us to treat a type as if it was a parameterised type (type with a single "hole", like `Functor`). We can freely treat values of type `A` as values of type `Id[A]`, and vice-versa. Values of `Id[T]` can be compared with unadorned values of type `T`:

```scala
val anId: Id[Int] = 42
anId should be(42)
```

## Xor

We can communicate an error by making it explicit in the data type we return. In general, `Validated` is used to **accumulate** errors, while `Xor` is used to **short-circuit** a computation upon the *first* error.

`scala.util.Either` lacks `flatMap` and `map` methods. In order to `map` over an `Either[A, B]` value, we have to state which side we want to `map` over. For example, if we want to map `Either[A, B]` to `Either[A, C]` we would need to `map` over the *right* side. This can be accomplished by using the `Either#right` method, which returns a `RightProjection` instance. `RightProjection` does have `flatMap` and `map` on it, which acts on the *right* side and *ignores the left* - this property is referred to as **right-bias**.

**Xor is right-biased.** (also, `Either` is right-biased starting from Scala **2.12**)

Type parameters of `Xor` are **covariant**, so when the compiler sees an `Xor[E1, A1]` and an `Xor[E2, A2]`, it will happily try to **unify** the `E1` and `E2` in a `flatMap` call and use the closest common supertype - `Object`, leaving us with practically no type information to use for pattern matching on the error in left side of `Xor`.


#Functors

functor | argument arrangement
--- | ---
covariant | `A => B`
contravariant | `B => A`
exponential | `(A => B, B => A)`
applicative | `F[A => B]`
monad | `A => F[B]`
comonad | `F[A] => B`


