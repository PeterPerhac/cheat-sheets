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

## Apply

`Apply` extends the `Functor` type class, adding method `ap`.
The `ap` function is similar to `map` in that we are transforming **a value in a context** (a *context* being the `F` in `F[A]`; a **context** can be `Option`, `List` or `Future` etc).



