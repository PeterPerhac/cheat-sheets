#Error Handling with Try

The function passed to `foreach` is executed only if the `Try` is a `Success`.

We can use `recover` to establish default behaviour in the case of a `Failure`. We don’t have to use `getOrElse`. `recover` expects a **partial function** and returns another `Try`. If `recover` is called on a `Success` instance, that instance is returned *as-is*. Otherwise, if the partial function is defined for the given `Failure` instance, its result is returned as a **Success**. If we want to swap a `Failure` for a different `Failure` (not a Success) we can use `recoverWith`.

