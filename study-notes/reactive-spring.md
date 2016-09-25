# Reactive Programming with the Spring Framework

Reactive Programming is a style of micro-architecture involving intelligent routing and consumption of events, all combining to change behaviour.

The basic idea behind reactive programming is that there are certain datatypes that represent a value "over time". Computations that involve these changing-over-time values will themselves have values that change over time.

## using Reactor Core v.3

* Flux is a publisher of a sequence of events of a specific POJO type, so it is generic, i.e. Flux<T> is a publisher of T
* Mono is a type representing a single valued or empty Flux. Mono has a very similar API to Flux but more focused because not all operators make sense for single-valued sequences.
* The empty sequence in Reactor is Mono<Void>.

To make the data flow you have to subscribe to the Flux using one of the subscribe() methods. Only those methods make the data flow. 

A batching subscriber is such a common use case that there are convenience methods already available in Flux. 
```
Flux.just("red", "white", "blue").log().map(String::toUpperCase).subscribe(null, 2);
```
### Threads, Schedulers and Background Processing

Reactor is extremely frugal with threads, because that gives you the greatest chance of the best possible performance. In the absence of any imperative to switch threads, even if the JVM is optimized to handle threads very efficiently, it is always faster to do computation on a single thread. Reactor has handed you the keys to control all the asynchronous processing, and it assumes you know what you are doing.

You can configure the subscriptions to be handled in a background thread using Flux.subscribeOn():
```
Flux.just("red", "white", "blue").map(String::toUpperCase).subscribeOn(Schedulers.parallel()).subscribe(null, 2);
```

There is another way to subscribe to a sequence, which is to call Mono.block() or Mono.toFuture() or Flux.toStream() (these are the "extractor" methods — they get you out of the Reactive types into a less flexible, blocking abstraction). Flux also has converters collectList() and collectMap() that convert from Flux to Mono.
A good rule of thumb is "never call an extractor". There are some exceptions (otherwise the methods would not exist). One notable exception is in tests because it’s useful to be able to block to allow results to accumulate.


