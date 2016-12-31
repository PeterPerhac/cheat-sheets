# Undercore Essential Scala course

## day 1 (1st November 2016)

to turn warnings into errors. make the code not compile if not proper.

```
 scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
```


## day 2 (2nd November)

sortWith
    takes two parameters and returns a Boolean
        this boolean is TRUE if the two parameters are in correct order already
        or FALSE, if the are not in the correct order

Type Class pattern - concept - in Scala done with implicits
type class always a trait (possibly with type parameters)
entry point will be an implicit parameter on a method

serialization to JSON is a candidate for a type class - apply the same behavious to a lot of different types

# JSON

- there's an overloaded version of mkString, used like so:

```
    List(1, 2, 3).mkString("(", "; ", ")") = "(1; 2; 3)"
```
