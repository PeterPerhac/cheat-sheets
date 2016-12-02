# Functional Programming in Scala

## What is FP

Based on a premise that we programs are constructed using only *pure functions* - i.e. functions that have *no side effects*. Functions that do something other than simply return a result is **not** a pure function.

Following the discipline of FP leads to increase in *modularity* - thus programs are easier to test, reuse, parallelize, generalize, and reason about.

**Procedure** is some parameterized chunk of code that may have side effects.

A **pure function** is **modular** and **composable** because it separates the logic of the computation itself from “what to do with the result” and “how to obtain the input”; Input is obtained in *exactly one way*: via the argument(s) to the function. And the output is simply computed and returned.

**Monomorphic** functions operate on only one type of data.
**Polymorphic** functions work for *any* type they're given.

