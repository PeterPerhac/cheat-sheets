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

### Identity monad

In order to allow to use non-monadic, plain values (without context) in places where a monad is expected, we can use the **identity** monad `Id`. This will allow us to abstract over monadic and non-monadic code.

```scala
import cats.Id
sumSquare(3 : Id[Int], 4 : Id[Int])
```

`Id[A]` is a type alias for `A`

Cats provides instances of various type classes for `Id`, including `Functor` and `Monad`. These let us call `map`, `flatMap` and so on on plain values.

The main use for `Id` is to write generic methods that operate on _monadic_ and _non-monadic_ data types. For example, we can run code asynchronously in production using `Future` and synchronously in test using `Id`.

Scala cannot **unify different shapes of type constructor** when searching for implicits. We need to cast to `Id[A]` in the call to function that expects a monadic input;

```scala
doSomethingWithMonads(Option(3), Option(4))
doSomethingWithMonads(3 : Id[Int], 4 : Id[Int])
```

**Swapping control flow**
If  we want to run a sequence of steps **until one succeeds**. We can model this using `Xor` (or `Either`) by flipping the `left` and `right` cases. The `swap` method provides this:

```scala
val a = 123.right[String]
// a: cats.data.Xor[String,Int] = Right(123)
val b = a.swap
// b: cats.data.Xor[Int,String] = Left(123)
```

## Eval monad

`cats.Eval` is a monad that allows us to abstract over different models of evaluation. We typically hear of two such models: **eager** and **lazy**. `Eval` throws in a further distinc on of **memoized** and **unmemoized** to create three models of evaluation:

 - _now_ — evaluated once immediately (equivalent to `val`)
 - _later_ — evaluated once when the value is first needed (equivalent to `lazy val`)
 - _always_ — evaluated every time the value is needed (equivalent to `def`)

While the semantics of the originating `Eval` instances are maintained, mapping functions are always called lazily on-demand (`def` semantics).

### Eval and stack-safety

`map` and `flatMap` methods are trampolined, meaning we can nest calls to `map` and `flatMap` arbitrarily without consuming stack frames.

Using `Eval.defer` for making recursive or trampolining calls will make the operation stack-safe. Like `map`/`flatMap` the `defer` method is also trampolined.

```scala
def factorial(n: BigInt): Eval[BigInt] = if (n == 1) Eval.now(n)
    else Eval.defer(factorial(n - 1).map(_ * n))
println(factorial(50000).value)
```

note the evaluation of the _recursive call_ is deferred.

## Writer monad

Useful for carrying a **log** along with a computation.

One common use for `Writers` is recording sequences of steps in muli-threaded computations, where standard imperative logging techniques can result in interleaved messages from different contexts. With `Writer` the log for the computation is tied to the result, so we can run concurrent computations without mixing logs.

The log in a `Writer` is preserved when we `map` or `flatMap` over it. `flatMap` actually appends the logs from the source `Writer` and the result of the user’s sequencing function. For this reason it’s good practice to use a log type that has an efficient _append_ and _concatenate_ operations, such as a `Vector`.

If we have a **log** and _no result_, we can create a `Writer[Unit]` using the `tell` syntax from `cats.syntax.writer`

In addition to transforming the result with `map` and `flatMap`, we can transform the log in a `Writer` with the `mapWritten` method.

We can tranform both log and result simultaneously using `bimap` or `mapBoth`: `bimap` takes two function parameters, one for the log and one for the result. `mapBoth` takes a single function that accepts two parameters.

We can clear the log with the `reset` method and swap log and result with the `swap` method.

## Reader monad

`cats.data.Reader` is a monad that allows us to compose operations that depend on some input. Instances of `Reader` wrap up functions of one argument, providing us with useful methods for composing them.
One common use for `Reader`s is injecting configuration. If we have a number of operations that all depend on some external configuration, we can chain them together using a Reader. The Reader produces one large operation that accepts the configuration as a parameter and runs our program in the order we specified it.


`flatMap` can be viewed as sequencing computations, giving the order in which operations must happen. `Option` represents a computation that can fail without an error message; `Either` represents computations that can fail with a message; `List` represents multiple possible results; and `Future` represents a computation that may produce a value at some point in the future.



## Monad transformers

 - OptionT
 - XorT
 - ReaderT
 - WriterT
 - StateT
 - IdT

Monad transformers follow the same convention: first type parameter specifies the monad that is wrapped around the monad _implied by the transformer_. The remaining type parameters are the types we’ve used to form the corresponding monads.

`cats.data.Kleisli` and `ReaderT` are, in fact, the same thing: `ReaderT` is actually a type alias for `Kleisli`.



## Cartesians and Applicatives

Whereas `Semigroup`s allow us to join **values**, `Cartesian`s allow us to join **contexts**.

```scala
import cats.Cartesian
import cats.instances.option._ 
Cartesian[Option].product(Some(123), Some("abc")) 
// res0: Option[(Int, String)] = Some((123,abc))
```

It's important to note that if _either_ parameter evaluates to `None`, the entire result is `None`

The idiomatic way of writing builder syntax is to combine `|@|` and `tupled` in a single expression:

```scala
( Option(1) |@| Option(2) |@| Option(3)).tupled
// res11: Option[(Int, Int, Int)] = Some((1,2,3))
```

Because `Xor` is a `Monad`, we know that the semantics of `product` are the same as those for `flatMap`. In fact, it is impossible for us to design a monadic data type that implements error accumulating semantics without breaking the consistency rules between these two methods.
Fortunately, Cats provides another data type called `Validated` that has an instance of `Cartesian` but **no** instace of `Monad`. The implementation of product is therefore free to accumulate errors.


### Validated

Neat way of creating instances of Validated using the cats-provided syntax:

```scala
import cats.syntax.validated._
123.valid[String]
// cats.data.Validated[String,Int] = Valid(123)
"error".invalid[Int]
// cats.data.Validated[String,Int] = Invalid(error)
```

Also there are various methods for conveniently creating instances of `Validated` from various other data types: `Exception`, `Try`, `Either`, `Option`

```scala
Validated.catchOnly[NumberFormatException]("foo".toInt)
Validated.catchNonFatal(sys.error("Badness"))
Validated.fromTry(scala.util.Try("foo".toInt))
Validated.fromEither[String, Int](Left("Badness"))
Validated.fromOption[String, Int](None, "Badness")
```

We can combine instances of `Validated` using `product`, `map2..22`, cartesian builder syntax, etc. All of these techniques require an appropriate `Cartesian` to be in scope. We need to fix the error type to create a type constructor with the correct number of parameters for `Cartesian`:

```scala
type ErrorsOr[A] = Validated[String, A]
```

`Validated` accumulates errors using a `Semigroup`, so we need one of those in scope to summon the `Cartesian`. If we don’t have one we get an annoyingly unhelpful compilation error:

```scala
Validated.fromOption[String, Int](None, "Badness")
Cartesian[ErrorsOr]
// error: could not find implicit value for parameter instance: cats.Cartesian[ErrorsOr]
//        Cartesian[ErrorsOr]
//                 ^
```

We must import a `Semigroup[String]` to make it work.


`String` isn’t an ideal type for accumulating errors. We commonly use `List`s or `Vector`s instead.

The `cats.data` package also provides the `NonEmptyList` and `NonEmptyVector` types that prevent us failing without _at least one_ error.


We can’t `flatMap` because `Validated` isn’t a `Monad`. However, we can convert back and forth between `Validated` and `Either` using the `toEither` and `toValidated` methods.

As with `Either`, we can use the `ensure` method to fail with a specified error if a predicate does not hold:

```scala
 123.valid[String].ensure("Negative!")(_ > 0)
```

## Foldable and Traverse

 - `Foldable` abstracts the familiar `foldLeft` and `foldRight` operations
 - `Traverse` is a higher-level abstraction that uses `Applicatives` for easier iteration (than folding)


Folds are performed with an _accumulator_ and a _binary function_.
Using a commutative binary operation such as (+) for folding we can use either `foldLeft` or `foldRight` and the result will be the same.
Using a non-commutative binary operation such as (-) will lead to potentially different results.


from Herding Cats:
Just like other monadic values, a **function** can also be considered a _value with a context_. The context for functions is that that value is not present yet and that we have to _apply_ that function to something in order to get its result value.



