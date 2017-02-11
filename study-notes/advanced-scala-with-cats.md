#Advanced Scala with Cats

## Higher Kinds

Kinds describe the number of “holes” in a type. We disnguish between regular types that have no holes, and _type constructors_ that have holes that we can fill to produce types.

Don't confuse type constructors with generic types. `List` is a **type constructor**, `List[A]` is a **type**.

Kind notation: Regular types are of kind `*`. The kind of `List` is `* => *` as it constructs a type from a single parameter. The kind of `Either` is `* => * => *` as it requires two parameters to construct a type.


Functor can be used to `lift` functions into a context, i.e. from `A => B` to `F[A] => F[B]` as long as there is a functor instance for the desired context (i.e. `_` hole of the functor) in scope.

```scala
import cats.instances.option._
val func = (x: Int) => s"___${x}___"
val lifted = Functor[Option].lift(func)
println(lifted(Option(1)))
// Some(___1___)
```

`Options` and `Lists` have their own built-in `map` operations.**If there is a built-in method it will always be called in preference to an extension method.** 


```scala
import cats.instances.function._
import cats.syntax.functor._
```

Allows us to call `map` on functions, as it brings implicit definitions into scope to make functors out of functions.


## Monads

A monad is a control mechanism for sequencing computations. Informally, a monad is anything with a `flatMap` method. The most important feature of a monad is its `flatMap` method, which allows us to specify what happens next. This is what we mean by sequencing computations.

We specify the application-specific part of the computation as a function parameter, and `flatMap` runs our function and takes care of some kind of complication (conventionally referred to as an _effect_).

Every monad is also a functor so we can rely on both `flatMap` and `map` to sequence computations that do and and don’t introduce a new monad. Plus, if we have both `flatMap` and `map` we can use _for comprehensions_ to clarify the sequencing behaviour.

We can run `Futures` in parallel, but that is another story. Monads are truly **all about sequencing**.

The monad behaviour is formally captured in two operations:
 - an operation **pure** with type `A => F[A]`
 - an operation **flatMap** with type `(F[A], A => F[B]) => F[B]`

In some languages and libraries, notably _Haskell_ and _Scalaz_, `flatMap` is referred to as `bind`. This is purely a _difference in terminology_.

### cats instances and syntax

Cats provides instances for all the monads in the standard library (`Option`, `List`, `Vector` and so on) via `cats.instances`

The syntax for monads comes from three places:
 - `cats.syntax.flatMap` provides syntax for `flatMap`
 - `cats.syntax.functor` provides syntax for `map`
 - `cats.syntax.applicative` provides syntax for `pure`


```scala
import cats.syntax.applicative._
import cats.instances.option._
import cats.instances.list._
1.pure[Option] // Option[Int] = Some(1)
1.pure[List] // List[Int] = List(1)
```


