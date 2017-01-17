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

