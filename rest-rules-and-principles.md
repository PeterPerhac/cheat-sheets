#REST API Design - **Rulebook outline
## URIs
### URI Format
  - **Rule:** Forward slash separator (/) must be used to indicate a hierarchical relationship
  - **Rule:** A trailing forward slash (/) should not be included in URIs
  - **Rule:** Hyphens (-) should be used to improve the readability of URIs
  - **Rule:** Underscores should not be used in URIs
  - **Rule:** Lowercase letters should be preferred in URI paths
  - **Rule:** File extensions should not be included in URIs
### URI Authority Design
  - **Rule:** Consistent subdomain names should be used for your APIs
  - **Rule:** Consistent subdomain names should be used for your client developer portal
### URI Path Design
  - **Rule:** A singular noun should be used for document names
  - **Rule:** A plural noun should be used for collection names
  - **Rule:** A plural noun should be used for store names
  - **Rule:** A verb or verb phrase should be used for controller names
  - **Rule:** Variable path segments may be substituted with identity-based values
  - **Rule:** CRUD function names should not be used in URIs
### URI Query Design
  - **Rule:** The query component of a URI may be used to filter collections or stores
  - **Rule:** The query component of a URI should be used to paginate collection or store results

## HTTP/1.1
### Request Methods
  - **Rule:** GET and POST must not be used to tunnel other request methods
  - **Rule:** GET must be used to retrieve a representation of a resource
  - **Rule:** HEAD should be used to retrieve response headers
  - **Rule:** PUT must be used to both insert and update a stored resource
  - **Rule:** PUT must be used to update mutable resources
  - **Rule:** POST must be used to create a new resource in a collection
  - **Rule:** POST must be used to execute controllers
  - **Rule:** DELETE must be used to remove a resource from its parent
  - **Rule:** OPTIONS should be used to retrieve metadata that describes a resource’s available interactions
### Response Status Codes
  - **Rule:** 200 (“OK”) should be used to indicate nonspecific success
  - **Rule:** 200 (“OK”) must not be used to communicate errors in the response body
  - **Rule:** 201 (“Created”) must be used to indicate successful resource creation
  - **Rule:** 202 (“Accepted”) must be used to indicate successful start of an asynchronous action
  - **Rule:** 204 (“No Content”) should be used when the response body is intentionally empty
  - **Rule:** 301 (“Moved Permanently”) should be used to relocate resources
  - **Rule:** 302 (“Found”) should not be used
  - **Rule:** 303 (“See Other”) should be used to refer the client to a different URI
  - **Rule:** 304 (“Not Modified”) should be used to preserve bandwidth
  - **Rule:** 307 (“Temporary Redirect”) should be used to tell clients to resubmit the request to another URI
  - **Rule:** 400 (“Bad Request”) may be used to indicate nonspecific failure
  - **Rule:** 401 (“Unauthorized”) must be used when there is a problem with the client’s credentials
  - **Rule:** 403 (“Forbidden”) should be used to forbid access regardless of authorization state
  - **Rule:** 404 (“Not Found”) must be used when a client’s URI cannot be mapped to a resource
  - **Rule:** 405 (“Method Not Allowed”) must be used when the HTTP method is not supported
  - **Rule:** 406 (“Not Acceptable”) must be used when the requested media type cannot be served
  - **Rule:** 409 (“Conflict”) should be used to indicate a violation of resource state
  - **Rule:** 412 (“Precondition Failed”) should be used to support conditional operations
  - **Rule:** 415 (“Unsupported Media Type”) must be used when the media type of a request’s payload cannot be processed
  - **Rule:** 500 (“Internal Server Error”) should be used to indicate API malfunction
### HTTP Headers
  - **Rule:** Content-Type must be used
  - **Rule:** Content-Length should be used
  - **Rule:** Last-Modified should be used in responses
  - **Rule:** ETag should be used in responses
  - **Rule:** Stores must support conditional PUT requests
  - **Rule:** Location must be used to specify the URI of a newly created resource
  - **Rule:** Cache-Control, Expires, and Date response headers should be used to encourage caching
  - **Rule:** Cache-Control, Expires, and Pragma response headers may be used to discourage caching
  - **Rule:** Caching should be encouraged
  - **Rule:** Expiration caching headers should be used with 200 (“OK”) responses
  - **Rule:** Expiration caching headers may optionally be used with 3xx and 4xx responses
  - **Rule:** Custom HTTP headers must not be used to change the behavior of HTTP methods
### Media Type Design
  - **Rule:** Application-specific media types should be used
### Media Type Schema Versioning
  - **Rule:** Media type negotiation should be supported when multiple representations are available
  - **Rule:** Media type selection using a query parameter may be supported

## Representation Design
### Message Body Format
  - **Rule:** JSON should be supported for resource representation
  - **Rule:** JSON must be well-formed
  - **Rule:** XML and other formats may optionally be used for resource representation
  - **Rule:** Additional envelopes must not be created
### Hypermedia Representation
  - **Rule:** A consistent form should be used to represent links
  - **Rule:** A consistent form should be used to represent link relations
  - **Rule:** A consistent form should be used to advertise links
  - **Rule:** A self link should be included in response message body representations
  - **Rule:** Minimize the number of advertised “entry point” API URIs
  - **Rule:** Links should be used to advertise a resource’s available actions in a state-sensitive manner
### Media Type Representation
  - **Rule:** A consistent form should be used to represent media type formats
  - **Rule:** A consistent form should be used to represent media type schemas
### Error Representation
  - **Rule:** A consistent form should be used to represent errors
  - **Rule:** A consistent form should be used to represent error responses
  - **Rule:** Consistent error types should be used for common error conditions

## Client Concerns
### Versioning
  - **Rule:** New URIs should be used to introduce new concepts
  - **Rule:** Schemas should be used to manage representational form versions
  - **Rule:** Entity tags should be used to manage representational state versions
### Security
  - **Rule:** OAuth may be used to protect resources
  - **Rule:** API management solutions may be used to protect resources
### Response Representation Composition
  - **Rule:** The query component of a URI should be used to support partial responses
  - **Rule:** The query component of a URI should be used to embed linked resources
### JavaScript Clients
  - **Rule:** JSONP should be supported to provide multi-origin read access from JavaScript
  - **Rule:** CORS should be supported to provide multi-origin read/write access from JavaScript

## Principles
  - **Principle:** REST API designs differ more than necessary
  - **Principle:** A REST API should be designed, not coded
  - **Principle:** Programmers and their organizations benefit from consistency
  - **Principle:** A REST API should be created using a GUI tool

