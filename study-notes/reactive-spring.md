# Reactive Programming with the Spring Framework

Reactive Programming is a style of micro-architecture involving intelligent routing and consumption of events, all combining to change behaviour.

The basic idea behind reactive programming is that there are certain datatypes that represent a value "over time". Computations that involve these changing-over-time values will themselves have values that change over time.


Reactor Core v.3

Flux is a publisher of a sequence of events of a specific POJO type, so it is generic, i.e. Flux<T> is a publisher of T
Mono is a type representing a single valued or empty Flux. Mono has a very similar API to Flux but more focused because not all operators make sense for single-valued sequences.
The empty sequence in Reactor is Mono<Void>.

to ask for the internal events inside a Flux to be logged to standard out, you can call the .log() method.


To make the data flow you have to subscribe to the Flux using one of the subscribe() methods. Only those methods make the data flow. 

A batching subscriber is such a common use case that there are convenience methods already available in Flux. 

Flux.just("red", "white", "blue").log().map(String::toUpperCase).subscribe(null, 2);

Reactor is extremely frugal with threads, because that gives you the greatest chance of the best possible performance. In the absence of any imperative to switch threads, even if the JVM is optimized to handle threads very efficiently, it is always faster to do computation on a single thread. Reactor has handed you the keys to control all the asynchronous processing, and it assumes you know what you are doing.

You can configure the subscriptions to be handled in a background thread using Flux.subscribeOn():
```
Flux.just("red", "white", "blue").map(String::toUpperCase).subscribeOn(**Schedulers.parallel()**).subscribe(null, 2);
```

