# Play framework essentials

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


