#Pure Functions
 - only operate on their input parameters
 - most useful Pure Functions must take at least one parameter
 - a parameterless pure function may only return a constant (as it has no inputs to operate on)
 - all useful Pure Functions must return something
 - will always produce the same output given the same inputs
 - cannot change any external variables
 - have no side effects

multi-valued changes
    Functional Programming deals with changes to values in a record by making a copy of the record with the values changed. It does this efficiently without having to copy all parts of the record by using data structures that makes this possible.
single-valued changes (e.g. loop counters).
    Functional programming solves the single-valued change in exactly the same way, by making a copy of it.

Functional Programming uses recursion to do looping.
recursion accomplishes the same as the for loop by calling itself with a new start value and a new accumulator. It doesn’t modify the old values. Instead it uses new values calculated from the old.


Higher-order Functions either take functions as parameters, return functions or both.

#Closures
 - When a function is created, all of the variables in its scope at the time of creation are accessible to it for the lifetime of the function.
 - A function exists as long as there still a reference to it.
 - A closure is a function’s scope that’s kept alive by a reference to that function.

