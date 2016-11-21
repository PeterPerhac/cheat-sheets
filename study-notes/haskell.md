# Learn you a Haskell for Great Good

## Type classes

If a type is a part of a typeclass, that means that it supports and implements the behavior the typeclass describes.

Everything before the => sybmol is called a class constraint.
    ```
    ghci> :t (==)  
    (==) :: (Eq a) => a -> a -> Bool
    ```
    read: the function == is of type a -> a -> Bool where a is constrained to types in the Eq type class
The `elem` function has a type of `(Eq a) => a -> [a] -> Bool` because it uses `==` over a list to check whether some value we're looking for is in it.
If a function is comprised only of special characters, it's considered an infix function by default. If we want to examine its type, pass it to another function or call it as a prefix function, we have to surround it in parentheses.

Explicit type annotations with double colon ::


