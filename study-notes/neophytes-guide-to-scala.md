#Error Handling

##Try

The function passed to `foreach` is executed only if the `Try` is a `Success`.

We can use `recover` to establish default behaviour in the case of a `Failure`. We don’t have to use `getOrElse`. `recover` expects a **partial function** and returns another `Try`. If `recover` is called on a `Success` instance, that instance is returned *as-is*. Otherwise, if the partial function is defined for the given `Failure` instance, its result is returned as a **Success**. If we want to swap a `Failure` for a different `Failure` (not a Success) we can use `recoverWith`.

##Either

Either instance cannot be used directly like a collection, the way we are familiar with from `Option` and `Try`. This is because `Either` is designed to be **unbiased**.

`Try` is **success-biased**: it offers you `map`, `flatMap` and other methods that all work under the assumption that the `Try` is a `Success`, otherwise they just return `Failure` as-is.

The fact that `Either` is **unbiased** means that you first have to choose whether you want to work under the assumption that it is a `Left` or a `Right`. By calling `left` or `right` on an `Either` value, you get a `LeftProjection` or `RightProjection`, respectively, which are basically left- or right-biased wrappers for the `Either`.

`Either` **doesn’t** define a `map` method. We have to map over the left or right projection of `Either`.

#Futures

Scala’s `Future[T]`, residing in the scala.concurrent package, is a container type, representing a computation that is supposed to *eventually* result in a value of type `T`.

`Future` is a **write-once** container – after a future has been completed, it is effectively immutable. Also, the `Future` type only provides an interface for **reading** the value to be computed. The task of writing the computed value is achieved via a `Promise`. 

When you are writing the function you pass to `map`, you’re in the future, or rather in a possible future. That mapping function gets executed as soon as your `Future` instance has completed successfully. If it has not completed successfully, the mapping function will not execute.

`Future[T]` is **success-biased,** allowing you to use `map`, `flatMap`, `filter` etc. under the assumption that it will complete *successfully*. Sometimes, you may want to be able to work in this nice functional way for the timeline in which things go wrong. By calling the `failed` method on an instance of `Future[T]`, you get a **failure projection** of it, which is a `Future[Throwable]`. Now you can `map` that `Future[Throwable]`, for example, and your mapping function will only be executed if the original `Future[T]` has completed with a failure.

====
VIM register backup

"a   cw``^[P
"b   dt i****^[hP^[ww
"c   O```haskell^[jo```^[
"d   cw**^[P

