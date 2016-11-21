# Learn you a Haskell for Great Good

GHCi Glasgow Haskell Compiler (interactive environment) special commands:
 - :q to quit
 - :l to load module from a file
 - :r to reload
 - :t display the type of an expression
 - :i information about a type
 - :? help

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

### Type classes (essential)

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

    `fromIntegral :: (Num b, Integral a) => a -> b` takes an integral number and turns it into a more general number.

All tuples are also part of Bounded if the components of the tuple are also Bounded.

##Pattern matching

To do pattern matching in ghci you would have to fit the whole on a single line like so:

    Prelude> lucky n | n==7 = "lucky" | otherwise = "not so lucky this time"

or write code in a separate file, then load it into your ghci session using the :l command. upon saving changes made to the file, these changes can be reloaded in the interpreter using the :r command.

`fst` and `snd` extract the components of pairs (tuples).
we can create functions that return whichever component like so:


    first :: (a, b, c) -> a
    first (x, _, _) = x

    second :: (a, b, c) -> b
    second (_, y, _) = y

    third :: (a, b, c) -> c
    third (_, _, z) = z


We could implement our own head function like this:


    head' :: [a] -> a
    head' [] = error "Can't call head on an empty list, dummy!"
    head' (x:_) = x


Guards play nicely with pattern matching. Here's an example of guards with a where clause:


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


Guards are indicated by pipes that follow a function's name and its parameters. Note that there's no = right after the function name and its parameters, before the first guard. Usually, they're indented a bit to the right and lined up.

