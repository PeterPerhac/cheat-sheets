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
 - :{ to go into multi line entry mode
 - :} to exit multi line entry mode and let GHCi parse user input

## Types and type classes

[Types and type classes - Learn you a Haskell] (http://learnyouahaskell.com/types-and-typeclasses)

Typeclasses can be used to achieve _ad hoc polymorphism_. If a type is a member of a typeclass, it supports and implements the behavior the typeclass prescribes.

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

## Syntax in Functions

[Syntax in Functions - Learn you a Haskell] (http://learnyouahaskell.com/syntax-in-functions)

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
The form is `let <bindings> in <expression>`. The names that you define in the let part are accessible to the expression after the in part.

Let bindings can be used to introduce functions in a local scope:

```haskell
[let square x = x * x in (square 5, square 3, square 2)]
```

Let bindings are very useful for quickly dismantling a tuple into components and binding them to names.

```haskell
(let (a,b,c) = (1,2,3) in a+b+c) * 100
```

We can use let bindings in list comprehensions earily - predicates to the right of the let binding can access the names bound-to in the let binding.

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


## Recursion

[Recursion - Learn you a Haskell] (http://learnyouahaskell.com/recursion)

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

## Higher Order Functions

[Higher order functions - Learn you a Haskell] (http://learnyouahaskell.com/higher-order-functions)

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

Note, there is the identity funciton `id` defined in Prelude 

## Modules

[Modules - Learn you a Haskell] (http://learnyouahaskell.com/modules)

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
- `take 10 $ iterate (*2) 1`                            .....   [1,2,4,8,16,32,64,128,256,512]
- `splitAt 3 "heyman"`                                  .....   ("hey","man")
- `takeWhile (>3) [6,5,4,3,2,1,2,3,4]`                  .....   [6,5,4]
- `dropWhile (/='P') "Hello, my name is Peter"`         .....   "Peter"
- `group "Peek a boo"`                                  .....   ["P","ee","k"," ","a"," ","b","oo"]

`foldl'` and `foldl1'` are stricter versions of their respective lazy incarnations. If you ever get stack overflow errors when doing lazy folds, try switching to their strict versions.

`and` takes a list of boolean values and returns `True` only if all the values in the list are `True`. Similarly, there's `or` function.
`any` and `all` take a predicate to test a list in a similar fashion as `and` and `or` - these two functions are *preferable* to mapping over a list and then doing `and` or `or`.

Note that `iterate` will iterate to form an infinite list, so you must limit this somehow by, for example, doing a `take` from it.

`span` is a kind of `takeWhile` that returns a tuple where `fst` is what `takeWhile` would have returned and `snd` is what would have been dropped.

```haskell
let (good, bad) = span (<=3) [1,2,3,4,5] in "Matching:" ++ (show good) ++ ", discarded:" ++ (show bad)
```

Doing `break p` is the equivalent of doing `span (not . p)`

```haskell
let (good, bad) = break (>3) [1,2,3,4,5] in "Matching:" ++ (show good) ++ ", discarded:" ++ (show bad)
```

`sort` will sort a List of `Ord`s

`group` takes a list and groups *adjacent* elements into sublists if they are equal.

### Maps

effectively lists of associations (key-value tuples)

It's usually better to use folds for standard list recursion pattern instead of explicitly writing the recursion because they're easier to read and identify. Everyone knows it's a fold when they see the foldl or foldr call, but it takes some more thinking to read explicit recursion.

```haskell
import qualified Data.Map as Map  
Map.fromList [("betty","555-2938"),("bonnie","452-2928"),("lucille","205-2928")]
```

That's an essential constraint in the Data.Map module. It needs the keys to be orderable so it can arrange them in a tree.

`null` checks if a map is empty.

`keys` and `elems` return lists of keys and values respectively. `keys` is the equivalent of `map fst . Map.toList` and `elems` is the equivalent of `map snd . Map.toList`.

`Data.Map`s don't allow duplicate keys, and while we can happily have tuples (associations) in a list that have same `fst` components, when converting these association lists to Maps, we'll lose some data. To avoid losing data, we could use this handy function:

```haskell
listToMap = Map.fromListWith (\n1 n2 -> n1 ++ ", " ++ n2) 
```

This will, on encountering an existing key, invoke the anonymous funcion to create a new value to store under that key.

### Sets

Similarly, like Maps, do a qualified import:

```haskell
import qualified Data.Set as Set
let set1 = Set.fromList "Some text is list of chars"
```

We can use standard set operations such as `intersection`, `difference`, `union`, `member`.

### Creating modules

At the beginning of a module, we specify the module name. If we have a file called Geometry.hs, then we should name our module Geometry. Then, we specify the functions that it exports and after that, we can start writing the functions.

```haskell
module Geometry  
( sphereVolume  
, sphereArea  
) where  
  
sphereVolume :: Float -> Float  
sphereVolume radius = (4.0 / 3.0) * pi * (radius ^ 3)  
    
sphereArea :: Float -> Float  
sphereArea radius = 4 * pi * (radius ^ 2)
```

Modules can also be given a hierarchical structure. Each module can have a number of sub-modules and they can have sub-modules of their own. 


## Making our own Types and Typeclasses

[Making our own Types and Typeclasses - Learn you a Haskell] (http://learnyouahaskell.com/making-our-own-types-and-typeclasses)


## Algebraic Data Types

We can use the `data` keyword to create a new data type.

```haskell
data Bool = False | True
data Int = -2147483648 | -2147483647 | ... | -1 | 0 | 1 | 2 | ... | 2147483647
```

The parts after the `=` are **value constructors**.

```haskell
data Shape = Circle Float Float Float | Rectangle Float Float Float Float
```

In the above example, a Shape can be either a Circle or a Rectangle. Circle's value constructor has three fields that take floats.

When we write a value constructor, we can optionally add some types after it and those types define the values it will contain. Value constructors are actually functions that ultimately return a value of a data type.

```haskell
ghci> :t Circle
Circle :: Float -> Float -> Float -> Shape
```

From the above we can see that in order to create a `Circle` shape, we need to apply `Circle` to three float arguments. Before we do this, let's define a function to calculate the surface of shapes:

```haskell
surface :: Shape -> Float
surface (Circle _ _ r) = pi * r ^ 2
surface (Rectangle x1 y1 x2 y2) = (abs $ x2 - x1) * (abs $ y2 - y1)
ghci> surface $ Circle 10 20 10  -- note here how a Shape is created - in this case a Circle value
314.15927
ghci> surface $ Rectangle 0 0 100 100  -- and a different kind of Shape
10000.0
```

We couldn't write a type declaration of `Circle -> Float` because `Circle` is not a type, `Shape` is.
Shapes still don't print nicely in the repl, so we need to make `Shape` type part of the `Show` typeclass, like so:

```haskell
data Shape = Circle Float Float Float | Rectangle Float Float Float Float deriving (Show)
```

If we add `deriving (Show)` at the end of a data declaration, Haskell automagically makes that type part of the Show typeclass.

We can improve on the shapes example by introducing Point data type and adding some useful methods and wrapping these types and functions in a module:

```haskell
module Shapes
( Point(..)
, Shape(..)
, surface
, nudge
, baseCircle
, baseRect
) where

data Point = Point Float Float deriving (Show)
data Shape = Circle Point Float | Rectangle Point Point deriving (Show)

surface :: Shape -> Float
surface (Circle _ r) = pi * r ^ 2
surface (Rectangle (Point x1 y1) (Point x2 y2)) = (abs $ x2 - x1) * (abs $ y2 - y1)

nudge :: Shape -> Float -> Float -> Shape
nudge (Circle (Point x y) r) a b = Circle (Point (x+a) (y+b)) r
nudge (Rectangle (Point x1 y1) (Point x2 y2)) a b = Rectangle (Point (x1+a) (y1+b)) (Point (x2+a) (y2+b))

baseCircle :: Float -> Shape
baseCircle r = Circle (Point 0 0) r

baseRect :: Float -> Float -> Shape
baseRect width height = Rectangle (Point 0 0) (Point width height)
```

Note that in the module definition we exported *all* value constructors of Point and Shape by writing `(..)`. We could have also listed them out explicitly, like ,for example, `Shape (Rectangle, Circle)`

There's the *record syntax* for more complicated data types:

```haskell
data Person = Person { firstName :: String
                     , lastName :: String
                     , age :: Int
                     , height :: Float
                     , phoneNumber :: String
                     , flavor :: String
                     } deriving (Show)
```

The resulting data type is exactly the same as `data Person = Person String String Int Float String String deriving (Show)`. By using record syntax to create this data type, Haskell automatically made these functions: `firstName`, `lastName`, `age`, `height`, `phoneNumber` and `flavor`. *Also* the format of String produced by `show` will automatically be improved.

### Type parameters

```haskell
data Maybe a = Nothing | Just a
```

Here we can see that a data type is parameterised with `a` which is (in the above case) not constrained in any way, so effectively any type would be good as an argument.
In the case of parameterised types, we would refer to `Maybe` as a type constructor. No value can have a type of `Maybe`, because that's not a type per se, it's a **type constructor**. It is used to construct an actual type like `Maybe String` or `Maybe Circle` which could have **values** of `Nothing` or `Just String` (or `Just Circle` depending on which data type was constructed).

```haskell
ghci> :t Just "Haha"
Just "Haha" :: Maybe [Char]
```

It's a very strong convention in Haskell to **never add typeclass constraints in data declarations** (though it *is* technically possible, e.g. `data (Ord k) => Map k v = ...`) Don't put type constraints into data declarations even if it seems to make sense, because you'll have to put them into the function type declarations either way.

When we derive the `Eq` instance for a type and then try to compare two values of that type with `==` or `/=,` Haskell will see if the **value constructors** match and then it will check if all the data **contained inside** matches by testing *each pair* of fields with `==.` There's only one catch though, the **types of all the fields also have to be part of the Eq typeclass**.


```haskell
data Bool = False | True deriving (Ord)
```
Because the `False` value constructor is specified **first** and the `True` value constructor is specified after it, we can consider `True` as **greater** than `False`.

In the `Maybe a` data type, the `Nothing` value constructor is specified **before** the `Just` value constructor, so a value of `Nothing` is always **smaller** than a value of `Just something`.

Here's an exmaple with a whole lot of type classes derived:

```haskell
data Day =  Monday | Tuesday | Wednesday | Thursday | Friday | Saturday | Sunday
            deriving (Eq, Ord, Show, Read, Bounded, Enum)
```

Monday is less than Tuesday, each `Day` value can be converted to/from its String representation thanks to `Show` and `Read`, and since it's also deriving from Enum, we can get predecessors and successors of days and we can make list ranges from them! (and thanks to `Bounded`, other fancy stuff too) yo, check it:

```haskell
ghci> [Thursday .. Sunday]
[Thursday,Friday,Saturday,Sunday]
ghci> [minBound .. maxBound] :: [Day]
[Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday]
ghci> [Monday .. ]
[Monday,Tuesday,Wednesday,Thursday,Friday,Saturday,Sunday]
ghci> reverse [Friday, Thursday .. ]
[Monday,Tuesday,Wednesday,Thursday,Friday]
```

### Type synonyms

The `[Char]` and `String` types are equivalent and interchangeable. That's implemented with **type synonyms**.

```haskell
type String = [Char]
```

Type synonyms can also be parameterized. If we want a type that represents an association list type but still want it to be general so it can use any type as the keys and values, we can do this:

```haskell
type AssocList k v = [(k,v)]
```

`Maybe` is a **type constructor**. When we apply an extra type to `Maybe`, like `Maybe String`, then we have a **concrete type**. Values can only have types that are *concrete types*.

### Recursive data structures

```haskell
infixr 5 :-:
data List a = Empty | a :-: (List a) deriving (Show, Read, Eq, Ord)
```

New syntactic construct for **fixity** declarations. We may want to assing fixity to functions defined as **operators**. A fixity states how tightly the operator binds and whether it's left- or right-associative. For instance, `*`'s fixity is `infixl 7 *` and `+`'s fixity is `infixl 6 +`. That means that they're both left-associative but `*` binds **tighter** than `+`, because it has a greater fixity.

### Functors

The `Functor` typeclass is defined as:

```haskell
class Functor f where
    fmap :: (a -> b) -> f a -> f b
```

What this effectively means is that a type that is a functor will need to provide an `fmap` function that allows the functor of type `a` to be mapped to a functor of type `b`. Note that functor `f` **requires** a type argument. Individual functor instances will be defined in terms of the functor's abstract type but leaving the type argument out of the picture. This means that functor `f` is a type constructor that takes one type parameter (like `Maybe`) not a concrete type (like `Just String`).

The List (`[]`) type **is** a functor. Its `map` function is of type:
```haskell
map :: (a -> b) -> [a] -> [b]
```

which conforms to the prescribed form of the `Functor`'s `fmap` function. Indeed, the **instance** of `Functor []` is simply just linking the prescribed `fmap` function to the `map` function provided by `[]`, and voila! List is now a member of the `Functor` typeclass:

```haskell
instance Functor [] where
    fmap = map
```

Any parameterised type could in theory try to be a `Functor`. Usually **container types** (boxes, like `[]` or `Maybe` or `Either`) will also be in the `Functor` typeclass:

```haskell
instance Functor Maybe where
    fmap f (Just x) = Just (f x)
    fmap f Nothing = Nothing
```

Functors must obey certain laws. If we use `fmap (+1)` over the list `[1,2,3,4]`, we expect the result to be `[2,3,4,5]` and not its reverse, `[5,4,3,2]`. If we use `fmap (\a -> a)` (the identity function) over some list, we expect to get back the same list as a result.

## Input and Output

[Input and Output - Learn you a Haskell] (http://learnyouahaskell.com/input-and-output)

Hello world application written in Haskell would look like this:

```haskell
main = putStrLn "hello, world"
```

if the above was in a file called helloworld.hs you could compile it using the ghc compiler, using the command:

```bash
$ ghc --make helloworld
```

The empty tuple is a value of `()` and it also has a type of `()`.

An I/O action will be performed when we give it a name of main and then run our program.

Having your whole program be just one I/O action seems kind of limiting. That's why we can use `do` syntax to glue together several I/O actions into one. Take a look at the following example:

```haskell
main = do
    putStrLn "Hello, what's your name?"
    name <- getLine
    putStrLn ("Hey " ++ name ++ ", you rock!")
```

`getLine` is an I/O action that contains a result type of `String`. The only way to open the IO "box" and get the data inside it is to use the `<-` construct. And if we're taking data out of an I/O action, we can only take it out when we're inside another I/O action.

Notice that we didn't bind the last `putStrLn` to anything. That's because in a `do` block, the last action cannot be bound to a name.

I/O actions will only be performed when they are given a name of `main` or when they're inside a bigger I/O action that we composed with a `do` block. We can also use a `do` block to glue together a few I/O actions and then we can use that I/O action in another `do` block and so on. Either way, they'll be performed only if they eventually fall into `main`.

In list comprehensions, the `in` part of a `let` binding isn't needed. You can use `let` bindings in `do` blocks pretty much like you use them in list comprehensions.

Use `<-` when you want to bind results of **I/O actions** to names and use `let` bindings to bind **pure expressions** to names.

You can use the `runhaskell` command like so: `runhaskell helloworld.hs` and your program will be executed on the fly.

The `return` in Haskell is really **nothing like** the `return` in most other languages! In Haskell `return` makes an I/O action out of a **pure value**.

`print` function is basically like `putStrLn . show` - it runs show on a value of any type in the `Show` typeclass, then `putStrLn` the resulting `String` into the standard output.

`when` takes a boolean value and an I/O action if that boolean value is True, it returns the same I/O action that we supplied to it. However, if it's False, it returns the `return ()` action: an I/O action that doesn't do anything.
