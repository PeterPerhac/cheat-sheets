# Functional and reactive domain modeling

## Introduction

### Entities and Value Objects
Every account has an identity that has to be managed in the course of its entire lifetime within the system. We refer to such elements as **entities.** For an account, its identity is its account number. Many of its attributes may change during the lifetime of the system, but the account is always identified with the specific account number that was allocated to it when it was opened. Two accounts in the same name and having the same attributes are considered different entities because the account numbers differ.

Each account may have an address - the residential address of the account holder. An address is *uniquely defined by the value that it contains*. You change any attribute of an address, and it becomes a different address. Can you identify the difference in semantics between an account and an address? An address doesn’t have any identity; it’s identified entirely based on the value it contains. Not surprisingly, we call such objects value objects. Another way to distinguish between entities and value objects is that value objects are immutable - you can’t change the contents of a value object without changing the object itself, after you create it.

The best way to differentiate between entities and value objects is to remember that an **entity has an identity that can’t change**, and a **value object has a value that can’t change**.

Every object (entity or value object) that you have in any model must have a definite lifecycle pattern. For every type of object you have in your model, you must have defined ways to handle: creation, participation in behaviors, persistence.


One of the entities within the aggregate (*consistency boundary* of an object graph) forms the *aggregate root*. It’s sort of the guardian of the entire graph and serves as the single point of interaction of the aggregate with its clients. It has two objectives to enforce:

 - Ensure the consistency boundary of business rules and transactions within the aggregate.
 - Prevent the implementation of the aggregate from leaking out to its clients, acting as a façade for all the operations that the aggregate supports.



