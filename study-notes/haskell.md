# Learn you a Haskell for Great Good

GHCi Glasgow Haskell Compiler (interactive environment) special commands:
 - :q to quit
 - :l to load module from a file
 - :r to reload
 - :t display the type of an expression
 - :i information about a type
 - :? help
 - :! execute command your shell (outside of GHCi)
    - use it to clear the screen for example, like so `:! clear`

## Type classes

If a type is a part of a typeclass, that means that it supports and implements the behavior the typeclass describes.

Everything before the `=>` sybmol is called a _class constraint_.

    ghci> :t (==)
    (==) :: (Eq a) => a -> a -> Bool

read the above as the function `==` is of type `a -> a -> Bool` where `a` is constrained to types in the `Eq` type class
The `elem` function has a type of `(Eq a) => a -> [a] -> Bool` because it uses `==` over a list to check whether some value we're looking for is in it.
If a function is comprised only of special characters, it's considered an _infix function_ by default. If we want to examine its type, pass it to another function or call it as a prefix function, we have to surround it in parentheses.
Backtick turns a name to an infix operator. Not only can we call functions as infix with backticks, we can also define them using backticks. Sometimes it's easier to read that way.

e.g.

    myCompare :: (Ord a) => a -> a -> Ordering
    a `myCompare` b
        | a > b     = GT
        | a == b    = EQ
        | otherwise = LT


Explicit type annotations with double colon `::`. Type annotations are a way of explicitly saying what the type of an expression should be.

### Essential Type Classes

 - **Eq**
    - support equality testing with == or /=
 - **Ord**
    - adds support for oderding of Eq values. Prescribes functions such as `<`, `>`, `<=`, `>=`, `min` and `max`
 - **Show**
    - members of Show can be presented as Strings
 - **Read**
    - like Show, but other way around. Members of Read can be reconstructed from a String representation.
 - **Enum**
    - for sequentially ordered types and can be enumerated. Types in the Enum type class can be used in list ranges. Functions to get successor and predecessor values with `succ` and `pred`.
 - **Bounded**
    - bounded types have an upper and lower bound - `minBound` and `maxBound`
 - **Num**
    - members of Num are able to act like numbers. This means some functions like multiplication `*` is availanle to all kinds of numbers - Int, Integer, Float, Double. **Integral** type class of whole numbers, and **Floating** for bananas.

```haskell
    `fromIntegral :: (Num b, Integral a) => a -> b` takes an integral number and turns it into a more general number.
```

All tuples are also part of Bounded if the components of the tuple are also Bounded.

##Pattern matching

To do pattern matching in ghci you would have to fit the whole on a single line like so:

```haskell
    lucky n | n==7 = "lucky" | otherwise = "not so lucky this time"
```

or write code in a separate file, then load it into your ghci session using the :l command. upon saving changes made to the file, these changes can be reloaded in the interpreter using the :r command.

`fst` and `snd` extract the components of pairs (tuples).
we can create functions that return whichever component like so:


```haskell
    first :: (a, b, c) -> a
    first (x, _, _) = x

    second :: (a, b, c) -> b
    second (_, y, _) = y

    third :: (a, b, c) -> c
    third (_, _, z) = z
```


We could implement our own head function like this:


```haskell
    head' :: [a] -> a
    head' [] = error "Can't call head on an empty list, dummy!"
    head' (x:_) = x
```


Guards play nicely with pattern matching. Here's an example of guards with a where clause:


```haskell
    bmiTell :: (RealFloat a) => a -> a -> String
    bmiTell weight height
        | bmi <= skinny = "You're underweight, you emo, you!"
        | bmi <= normal = "You're supposedly normal. Pffft, I bet you're ugly!"
        | bmi <= fat    = "You're fat! Lose some weight, fatty!"
        | otherwise     = "You're a whale, congratulations!"
        where   bmi = weight / height ^ 2
                skinny = 18.5
                normal = 25.0
                fat = 30.0
```


Guards are indicated by pipes that follow a function's name and its parameters. Note that there's no = right after the function name and its parameters, before the first guard. Usually, they're indented a bit to the right and lined up.

Similar to `where` bindings are the `let` bindings. Similar, but not quite the same - their scope is narrower:


```haskell
    cylinder :: (RealFloat a) => a -> a -> a
    cylinder r h =
        let sideArea = 2 * pi * r * h
            topArea = pi * r ^2
        in  sideArea + 2 * topArea
```


Also, the `let` bindings are *expressions themselves*. `where` bindings are just syntactic constructs.
The form is let <bindings> in <expression>. The names that you define in the let part are accessible to the expression after the in part.

Let bindings can be used to introduce functions in a local scope:

```haskell
    [let square x = x * x in (square 5, square 3, square 2)]
```

Let bindings are very useful for quickly dismantling a tuple into components and binding them to names.

```haskell
    (let (a,b,c) = (1,2,3) in a+b+c) * 100
```

we can use let bindings in list comprehensions earily - predicates to the right of the let binding can access the names bound-to in the let binding.

```haskell
    calcBmis xs = [bmi | (w, h) <- xs, let bmi = w / h ^ 2, bmi >= 25.0]
```

Since let bindings *are* expressions and are fairly local in their scope, they can't be used across guards.

```haskell
    case expression of  pattern -> result
                        pattern -> result
                        pattern -> result
                        ...
```

Whereas pattern matching on function parameters can only be done when defining functions, case expressions can be used pretty much anywhere.


```haskell
    describeList :: [a] -> String
    describeList xs = "The list is " ++ case xs of [] -> "empty."
                                                   [x] -> "a singleton list."
                                                   xs -> "a longer list."
```


## Recursion

There's a `replicate` function that will repeat n-times any value x and return a list of xs.

Use guards instead of patterns when testing for a boolean condition.


```haskell
    take' :: (Num i, Ord i) => i -> [a] -> [a]
    take' n _
        | n <= 0   = []
    take' _ []     = []
    take' n (x:xs) = x : take' (n-1) xs
```


above is a function signature followed by three patterns. The first pattern is guarded.
When `take'` is applied to an `n` and _anything_ else where n is negative or zero, just return an empty list.
When `take'` is applied to _any_ `n` and an empty list, just return an empty list.
When `take'` is applied to an `n` and a something that fits the `x:xs` pattern (i.e. a non-empty list) then return the head of that list followed by a `take'` of one less element of the tail of the list.

an interesting implementation of quicksort:


```haskell
    quicksort :: (Ord a) => [a] -> [a]
    quicksort [] = []
    quicksort (x:xs) =
        let smallerSorted = quicksort [a | a <- xs, a <= x]
            biggerSorted = quicksort [a | a <- xs, a > x]
        in  smallerSorted ++ [x] ++ biggerSorted
```


using list comprehensions, generate lists of smaller and bigger numbers, stitch the whole whole thing together in the order that smaler numbers come before current number, and bigger numbers follow. Do this step recursively and you'll get the whole list in a sorted order.

### Syntax summary

```haskell
    1 : [2, 3] -- [1, 2, 3]
    'C' : "at" -- "Cat" (Remember strings are lists of characters)
    True : [] -- [True]
    1 : 2 : 3 : [] == [1, 2, 3] -- True

    elem 3 [1, 4, 73, 12] -- False
    'H' `elem` "Highgarden" -- True
    [1, 2] ++ [3, 4] -- [1, 2, 3, 4]
    "Hello " ++ "World" -- "Hello World"
    head [1, 2, 3] -- 1
    tail [1, 2, 3] -- [2, 3]
    last [1, 2, 3] -- 3
    init [1, 2, 3] -- [1, 2]
    "Casterly Rock" !! 3 -- 't'

    null [] -- True
    null [1, 2, 3] -- False
    length "Dorne" -- 5

    (1, 2)
    ("Hello", True, 2)
    fst (6, "Six") -- 6
    snd (6, "Six") -- "Six"
```

## Higher Order Functions

```haskell
    applyTwice :: (a -> a) -> a -> a
```

`->` is naturally right-associative. However, in the exmple above the brackets are mandatory. Parentheses indicate that the first parameter is a function that takes something and returns that same thing.

`length` returns an `Int` instead of a `Num` for historical reasons. If we wanted to return a more general Num, we could have used `fromIntegral`.


### Lambdas

Lambdas are expressions, and we can just pass them around. The expression `(\xs -> length xs > 15)` returns a function that tells us whether the length of the list passed to it is greater than 15.

Syntax overview:

```haskell
    (\x -> x + x)
    (\x y -> x + y)
    (\x -> 10 + x) 5 -- 15
```


People who are not well acquainted with how currying and partial application works often use lambdas where they don't need to. For instance, the expressions `map (+3) [1,6,3,2]` and `map (\x -> x + 3) [1,6,3,2]` are equivalent since both `(+3)` and `(\x -> x + 3)` are functions that take a number and add 3 to it. Needless to say, making a lambda in this case is stupid since using partial application is much more readable.


We can pattern-match in lambdas, but only one pattern for a parameter (i.e. can't define several patterns for one parameter, like making a [] and a (x:xs) pattern for the same parameter and then having values fall through)

```haskell
    map (\(a,b) -> a + b) [(1,2),(3,5),(6,3),(2,6),(2,5)]
```


`++` function is much more expensive than `:` so we usually use *right* folds when we're building up *new lists* from a list.



