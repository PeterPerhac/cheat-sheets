How to create a clean (minimal) new project?

```
sbt new sbt/scala-seed.g8
```

this will prompt for project name, then create a directory of that name to contain the new project.



to add imports to routes file, first import the play settings key:
import play.sbt.routes.RoutesKeys.routesImport

then add the setting to your build, like so:

```scala
routesImport ++= Seq("my.import1", "my.other.package"), ...
```



find out the classpath of an application from sbt
```
sbt "export runtime:fullClasspath"
```
