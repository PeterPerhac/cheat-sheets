# Scala notes

To turn warnings into errors and to make the code not compile if not proper, add this to your SBT build file:

```scala
scalacOptions ++= Seq("-unchecked", "-deprecation", "-feature", "-Xfatal-warnings")
```

There's an overloaded version of `mkString`, used like so:

```scala
List(1, 2, 3).mkString("(", "; ", ")") = "(1; 2; 3)"
```

Also, `sortWith` is an interesting method for sorting stuff: It takes two parameters and returns:
        - TRUE if the two parameters are in *correct order* already
        - FALSE if they are not

Type Class concept in Scala is implemented with **implicits**. It is always a trait (possibly with type parameters) and the entry point will be an implicit parameter on a method.

Serialization to/from JSON is a candidate for a type class - as we'd like to apply the same behaviour to a lot of different types.

Self-type declarations: we can explicitly assign a type to `this` like so:

```scala
trait Something { self : ExplicitType =>
 def foo = "bar"
}
```

Self type of a trait is the **assumed** type of `this`, the receiver, to be used within the trait. **Any concrete class that mixes in the trait must ensure that its type conforms to the trait's self type.** The most common use of self types is for dividing a large class into several traits.

