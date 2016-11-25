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

```haskell
ghci> :t (==)
(==) :: (Eq a) => a -> a -> Bool
```

read the above as the function `==` is of type `a -> a -> Bool` where `a` is constrained to types in the `Eq` type class
The `elem` function has a type of `(Eq a) => a -> [a] -> Bool` because it uses `==` over a list to check whether some value we're looking for is in it.
If a function is comprised only of special characters, it's considered an _infix function_ by default. If we want to examine its type, pass it to another function or call it as a prefix function, we have to surround it in parentheses.
Backtick turns a name to an infix operator. Not only can we call functions as infix with backticks, we can also define them using backticks. Sometimes it's easier to read that way.

e.g.

```haskell
myCompare :: (Ord a) => a -> a -> Ordering
a `myCompare` b
    | a > b     = GT
    | a == b    = EQ
    | otherwise = LT
```


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
fromIntegral :: (Num b, Integral a) => a -> b` takes an integral number and turns it into a more general number.
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


We can pattern-match in lambdas, but only one pattern for a parameter (i.e. can't define several patterns for one parameter, like making a `[]` and a `(x:xs)` pattern for the same parameter and then having values fall through)

```haskell
map (\(a,b) -> a + b) [(1,2),(3,5),(6,3),(2,6),(2,5)]
```

`++` function is much more expensive than `:` so we usually use **right** folds when we're building up **new lists** from a list.

Folds can be used to implement any function where you traverse a list once, element by element, and then return something based on that. Whenever you want to traverse a list to return something, chances are you want a fold.

The `foldl1` and `foldr1` functions work much like `foldl` and `foldr`, only you don't need to provide them with an explicit starting value. They assume the first (or last) element of the list to be the starting value and then start the fold with the element next to it.


`scanl` and `scanr` are like `foldl` and `foldr`, only they report all the intermediate accumulator states in the form of a list. There are also `scanl1` and `scanr1`, which are analogous to `foldl1` and `foldr1`.

Scans are used to **monitor the progression of a function** that can be implemented as a `fold`. Let's answer us this question: *How many elements does it take for the sum of the roots of all natural numbers to exceed 1000?* Well, we can write the calculation as a fold, but the question is actually about the computation, not about the result of the computation.


```haskell
sqrtSums :: Int
sqrtSums = length (takeWhile (<1000) (scanl1 (+) (map sqrt [1..]))) + 1

ghci> sqrtSums
131
```
We use `takeWhile` here instead of `filter` because `filter` doesn't work on infinite lists. Even though we know the list is ascending, `filter` doesn't, so we use `takeWhile` to cut the scanlist off at the first occurence of a sum greater than 1000.

###Function application with $

The `$` function has the lowest precedence.
Function application with a space is **left**-associative (so `f a b c` is the same as `((f a) b) c)`), Function application with `$` is **right**-associative.

`sqrt 3 + 4 + 9` adds together 9, 4 and the square root of 3. If we want get the square root of 3 + 4 + 9, we'd have to write `sqrt (3 + 4 + 9)` or if we use `$` we can write it as `sqrt $ 3 + 4 + 9` because `$` has the lowest precedence of any operator. That's why you can imagine a `$` being sort of the equivalent of writing an opening parentheses and then writing a closing one on the far right side of the expression.

But apart from getting rid of parentheses, `$` means that function application can be treated just like another function. That way, we can, for instance, map function application over a list of functions.

```haskell
map ($ 3) [(4+), (10*), (^2), sqrt]  -- [7.0,30.0,9.0,1.7320508075688772]
```

### Function Composition

Composing two functions produces a new function that, when called with a parameter, say, `x` is the equivalent of calling `g` with the parameter `x` and then calling `f` with that result. (`f` **must** take as its parameter a value that has the same type as `g`'s return value.)

In Haskell this is done with the dot function `.`

```haskell
map (\xs -> negate (sum (tail xs))) [[1..5],[3..6],[1..7]]
map (negate . sum . tail) [[1..5],[3..6],[1..7]]
```

Note that the above expressions yield the same result, the second one is easier to read and uses function composition with `.`

Common use of function composition is defining functions in the so-called *point free style*. We can easily turn this function definition into a point free one like so:

```haskell
sum' xs = foldl (+) 0 xs
sum' = foldl (+) 0
```

but in order to turn the following into a point free function definition, function composition is required:

```haskell
fn x = ceiling (negate (tan (cos (max 50 x))))
fn = ceiling . negate . tan . cos . max 50
```

However, many times, writing a function in point free style can be less readable if a function is too complex. The prefered style is to use `let` bindings to give labels to intermediary results or split the problem into sub-problems and then put it together so that the function makes sense to someone reading it instead of just making a huge composition chain.


## Modules

The `Prelude` module is loaded by default.

The syntax for importing modules in a Haskell script is `import module name`. This must be done before defining any functions, so imports are usually done at the top of the file.

`Data.List` module, which has a bunch of useful functions for working with lists.

To load and unload modules in the interactive GHCi session:

```haskell
ghci> :m + Data.List Data.Map Data.Set
ghci> :m - Data.Set
```

If you just need a couple of functions from a module, you can selectively import just those functions. To import only the `nub` and `sort` functions from `Data.List` do this:

```haskell
import Data.List (nub, sort)
```

`nub` is used to remove duplicate elements from a list. Only first occurence of each element is kept.

We can *not* import certain functions from a module by *hiding* them:

```haskell
import Data.List hiding (nub)
```

To avoid name clashes of imported modules, we can do qualified imports (and alias them so it's not too tedious to fully qualify each function):

```haskell
import qualified Data.Map as M
```

*Hoogle* is a Haskell search engine that allows you to search Haskell standard library by name, module name or even type signature.


- `intersperse '.' "MONKEY"`                            .....   "M.O.N.K.E.Y"
- `intercalate ", " ["Apples", "Bananas", "Pears"]`     .....   "Apples, Bananas, Pears"
- `transpose [[1,2,3],[4,5,6],[7,8,9]]`                 .....   [[1,4,7],[2,5,8],[3,6,9]]
- `concat ["foo","bar","car"]`                          .....   "foobarcar"
- `concatMap (replicate 4) [1..3]`                      .....   [1,1,1,1,2,2,2,2,3,3,3,3]
- `concat $ (replicate 4) [1..3]`                       .....   [1,2,3,1,2,3,1,2,3,1,2,3]

`foldl'` and `foldl1'` are stricter versions of their respective lazy incarnations. If you ever get stack overflow errors when doing lazy folds, try switching to their strict versions.

`and` takes a list of boolean values and returns `True` only if all the values in the list are `True`. Similarly, there's `or` function.
`any` and `all` take a predicate to test a list in a similar fashion as `and` and `or` - these two functions are *preferable* to mapping over a list and then doing `and` or `or`.

continue at `iterate`
