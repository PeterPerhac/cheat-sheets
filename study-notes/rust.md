#Rust

> The `::` syntax in the `::new` line indicates that new is an _associated function_ of the String type. An associated function is implemented on a type, in this case String, rather than on a particular instance of a String. Some languages call this a _static method_.

The `Result` types are enumerations, often referred to as enums. The values are called the enum’s _variants_.
For `Result`, the variants are `Ok` or `Err`. The `Ok` variant indicates the operation was successful, and **inside** Ok is the successfully generated value. The `Err` variant means the operation failed, and Err contains information about how or why the operation failed.

`Ordering` is another enum, but the _variants_ for Ordering are `Less`, `Greater`, and `Equal`.

A **match** expression is made up of **arms**. An arm consists of a _pattern_ and the code that should be run.



