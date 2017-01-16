# Functional Programming in Scala

## What is FP

Based on a premise that we programs are constructed using only *pure functions* - i.e. functions that have *no side effects*. Functions that do something other than simply return a result is **not** a pure function.

Following the discipline of FP leads to increase in *modularity* - thus programs are easier to test, reuse, parallelize, generalize, and reason about.

**Procedure** is some parameterized chunk of code that may have side effects.

A **pure function** is **modular** and **composable** because it separates the logic of the computation itself from “what to do with the result” and “how to obtain the input”; Input is obtained in *exactly one way*: via the argument(s) to the function. And the output is simply computed and returned.

**Monomorphic** functions operate on only one type of data.
**Polymorphic** functions work for *any* type they're given.

### Algebraic Data Types

An ADT is just a data type defined by one or more data constructors, each of which may contain zero or more arguments. We say that the data type is the sum or union of its data constructors, and each data constructor is the product of its arguments, hence the name algebraic data type.

## Strictness and Laziness

### Strict and non-strict functions

A function is *non-strict* if it may choose not to evaluate one or more of its arguments.
In contrast, a *strict* function **always** evaluates its arguments.

Any function definition in Scala will be strict, unless we tell it otherwise.

A value of type `() => A` is a function that accepts zero arguments and returns an `A`. In general, the unevaluated form of an expression is called a **thunk**, and we can force the thunk to evaluate the expression and get a result.

In fact, the type `() => A` is a syntactic alias for the type `Function0[A]`.

scala provides nicer syntax for creating and accepting these parameterless functions - thunks: just prefix the arrow symbol `=>` to the type of the parameter to be taken non-strictly, **by name**.

```scala
def maybetwice(b: boolean, i: => int) = if (b) i+i else 0
```

in the above funciton, if `b` is false, i will never be evaluated.

```scala
scala> val x = maybetwice(true, { println("hi"); 1+41 })
hi
hi
x: int = 84
```

Note that since `maybetwice` refers to paramter `i` twice, the `println` is also evaluated twice. To avoid evaluating the expression twice, we may chose to cache its computation using a local `lazy val`. Adding the `lazy` keyword to a `val` declaration will cause Scala to **delay** evaluation of the right-hand side until it’s first referenced. It will also **cache** the result so that subsequent references to it don’t trigger repeated evaluation.

