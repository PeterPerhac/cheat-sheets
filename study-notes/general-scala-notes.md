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

a self type is an assumed type for this whenever this is mentioned within the class.
a self type specifies the requirements on any concrete class the trait is mixed into. 

Any concrete class that mixes in the trait must ensure that its type conforms to the trait's self type. The most common use of self types is for dividing a large class into several traits.


## `collect` vs `filter` followed by `map`

`collect` can generate a collection of appropriate target type inferred from the return type of the provided partial function. Achieving a collection of a different type can be ugly with `filter` and `map`. Consider this:

```scala
trait Base
class Foo extends Base
class Bar extends Base

Seq[Base](new Foo(), new Bar()) collect { case e:Foo => e } // returns Seq[Foo]
```
can be achieved with `filter` and `map` like this:

```scala
Seq[Base](new Foo(), new Bar()).filter(_.isInstanceOf[Foo]).map(_.asInstanceOf[Foo]) //returns Seq[Foo]
```

Also, `collect` performs filtering and function application _in one step_ and therefore is possibly more performant than stair-stepping with `filter` and `map`.

`filter` and `map` will be, on the other hand, sometimes more readable if you already have a predicate and a mapping function.

```scala
foo.filter(p).map(f)
//better than
foo.collect {case x if p(x) => f(x) }
```



