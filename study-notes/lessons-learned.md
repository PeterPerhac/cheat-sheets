one can import **dependent types** only from `val`s, not `def`s

The local import `col.BatchCommands.AggregationFramework._` is required, and cannot be replaced by a global static `import reactivemongo.api.collections.BSONCollection.BatchCommands.AggregationFramework._`. The type `.BatchCommands.AggregationFramework.AggregationResult` is a **dependent one**, used for the intermediary/MongoDB result, and must *not* be exposed as public return type in your application/API.
[http://reactivemongo.org/releases/0.12/documentation/advanced-topics/aggregation.html]

as long as `col` is a def, the import will not work!




An implicit value must have the word `implicit` in front of it in order to qualify for an implicitly provided argument. It's not enough to define a `val` like so:

```scala
 val bsonHandler = Macros.handler[Reading]
```

But the `val` must also be marked as `implicit` in order to get the sweet sweet benefits of it magically serializing a case class to BSON.

```scala
  implicit val bsonHandler = Macros.handler[Reading]
```


stupid single quotes in messages file caused argument interpolation to not work at all. single quotes are used to escape interpolation. just a single single quote will prevent interpolation to happen.
