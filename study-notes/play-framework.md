# Play framework essentials

## Controllers, Actions and Results



```
lazy val root = project.in(file(".")).enablePlugins(PlayScala)
```

The last line of `build.sbt` imports the default settings of Play projects defined by the Play sbt plugin from the `project/plugins.sbt` file. As the development of a Play application involves several **file generation tasks** (templates, routes, assets etc.), the sbt plugin helps managing these.

`conf/routes` file configures the Router (like a Spring DispatcherServlet that figures out how which controller to invoke based on @RequestMapping annotations),

Apart from comments (starting with #), each line of the `routes` file defines a route associating an HTTP **verb** and a URL **pattern** to a controller **action** call.

Path parameters are defined with a colon `/items/:id` and bound to identifiers (`id` in this case). This path definition will match the `/items/42` path, but the `/items/`, `/items/42/0`, or even `/items/42/` paths don't match. Each path parameter (defined with a colon) is bound to only one **path segment**. We can specify path parameters spanning several path segments using the star like so:

```scala
GET     /assets/*file        controllers.Assets.at(path = "/public", file)
```

Query string parameters (extracted from the URL query string) are routed into controller actions automatically. Query string parameters may also assume default values like so:

```scala
GET   /items        controllers.Items.list(page: Int ?= 1)
```

Force a path parameter to match a regular expression using the following syntax:

```scala
GET     /items/$id<\d+>    controllers.Items.details(id: Long)
```

Here, the path parameter would have to conform to the regex in order for the request to be routed to the details function on the Items controller. You'd get a 404 which would, while Play is in Dev mode, be nicely formatted page - "Action Not Found" - and a list of routes that were tried in order to service the request.

**Type coerction** happens if necessary to slot `String` parameters into `Int`/`Long` etc type function parameters on the controller methods. If it fails you get a 400 Bad Request. Type coercion logic is extensible in Play.

##Body parsers and JSON

In Play, the component responsible for interpreting the body of an HTTP request is named body parser. By default, actions use a tolerant body parser that will be able to parse the request content as JSON, XML, or URL-encoded form or multipart form data, but you can force the use of a specific body parser by supplying it as the first parameter of your action builder. The advantage is that within the body of your request, you are guaranteed that the request body (available as the body field on the request value) has the right type. If the request body cannot be parsed by the body parser, Play returns an error response with the status 400 (Bad Request).

In Scala, there is a body parser that not only parses the request body into a JSON blob but also validates it according to a reader definition and returns a 400 (Bad Request) response in the case of a failure.

```scala
val create = Action(parse.json[CreateItem]) { implicit request =>
    shop.create(request.body.name, request.body.price) match {
        case Some(item) => Ok(Json.toJson(item))
        case None => InternalServerError
    }
}
```

Reads defined in terms of themselves will lead to an infinite loop unless we give some thought when defining them. We can use the `lazyRead` combinator to defer parsing recursive data structures until after the current Reads definition is complete.

```scala
implicit val readsCategory: Reads[Category] = (
  (__ \ "name).read[String] and
  (__ \ "subcategories").lazyRead(Reads.seq[Category])
)(Category.apply _)
```

The `lazyRead` combinator is exactly the same as `read`, but uses a by-name parameter that is not evaluated until needed, thus preventing the infinite recursion in the case of recursive `Reads`.


# Side notes, edit later

## Futures
`Future.apply(None)` creates an asynchronous computation and executes it; i.e. extra lambda object is created and extra task is scheduled (however trivial). `Future.successful(None)` just produces an already-completed `Future`, hence it is more efficient.

## SBT:
 - `test` runs all tests
 - `test-only` <testClass> runs only that one test class
 - `test-quick` runs only failed tests
 - `~test-quick` keeps running failed tests continually on every change to source file(s)


