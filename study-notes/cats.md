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

Cats implementations of Monoid provide a combineAll method too:

```scala
Monoid[String].combineAll(List("a", "b", "c")) should be("abc")
```

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


## APPLICATIVE FUNCTORS & MONADS

`Applicative` is a generalization of `Monad`, allowing expression of effectful computations in a pure functional way.

`Applicative` is generally preferred to `Monad` when the structure of a computation is fixed a priori. That makes it possible to perform certain kinds of static analysis on applicative values.

```scala
Monad[Option].pure(1) should be(Some(1))
Applicative[Option].pure(1) should be(Some(1))
```

## Monad

`Monad` extends the `Applicative` type class with a new function `flatten`, which takes a value in a nested context (eg. `F[F[A]]` where `F` is the context) and *joins* the contexts together so that we have a single context (ie. `F[A]`).

