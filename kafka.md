go here for instructions on how to install kafka on CentOS

```bash
sudo passwd
/bin/su -
cd /opt
yum install wget
yum install tar
wget http://mirror.ox.ac.uk/sites/rsync.apache.org/kafka/0.11.0.0/kafka_2.11-0.11.0.0.tgz
tar -zxvf kafka_2.11-0.11.0.0.tgz
rm -f kafka_2.11-0.11.0.0.tgz
cd kafka_2.11-0.11.0.0.tgz
```

**leader** is the node responsible for all reads and writes for the given partition. Each node will be the leader for a randomly selected portion of the partitions.
**replicas** is the list of nodes that replicate the log for this partition regardless of whether they are the leader or even if they are currently alive.
**isr** is the set of "in-sync" replicas. This is the subset of the replicas list that is currently alive and caught-up to the leader.


## Zookeeper

Kafka uses ZooKeeper for basically **all the metadata,** with _topic offsets_ being one notable exception.

Kafka does require ZooKeeper. _It's not an option._

```bash
# start zookeeper
bin/zookeeper-server-start.sh config/zookeeper.properties &
# start broker server
bin/kafka-server-start.sh config/server.properties &
# configure replicated brokers
cp config/server.properties config/server-1.properties
# edit log.dirs property if running both kafka brokers on same machine
# start more brokers
# create a replicated topic
bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 3 --partitions 1 --topic my-replicated-topic
# show info about the status of a topic
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic my-replicated-topic
# manually add messages to a topic like so:
bin/kafka-console-producer.sh --broker-list localhost:9092,localhost:9093,localhost:9094 --topic my-replicated-topic
# and consume them from CLI like so:
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092,localhost:9093,localhost:9094 --topic my-replicated-topic --from-beginning
```

for the **producer**: `--broker-list` and `--topic` are required arguments
for the **consumer**: `--bootstrap-server` or `--zookeeper` is a required argument


Kafka can't be categorised as providing publish-subscribe XOR typical message queue semantics (where publish-subscribe means every subscriber receives a copy of a given message, while "typical message queue semantics" means each message is delivered to only one recipient).

## Consumer Groups
When a consumer subscribes to a topic it specifies a consumer group to subscribe as. The existence of a consumer group **is implied by using its name during subscription**; no explicit creation is required.

A consumer group can also be thought of as as _logical subscriber_ in publish-subscribe terminology.

The producer maintains connections with each node within the cluster that is the leader for a topic partition that the producer is pushing messages to.

## Delivery & processing guarantees

### cheap and cheerful
Offsets within a topic partition can be auto-committed by Kafka periodically - this gives the least processing guarantees. The periodic commits happen on Kafka-side and you don't know if they happened before or after the consumer's processing the message (which may have failed). This ways it's neither at-least-once nor at-most-once delivery.

Kafka itself stores offsets in **compacted topics**.

### at-least-once and at-most-once delivery
Offsets could be managed by Kafka, but the "when" offsets are committed can be controlled by the consumer. In this way the consumer decides whether to commit offset before or after processing, whereby deciding on at-most-once / at-least-once delivery. If committing before processing, the message will be delivered at most once. If committing after processing, the message would be consumed at least once.

### exactly-once delivery
Exactly-once delivery can be achieved by not relying on Kafka to store the topic offsets. Storing the offset in a separate persistent store, atomically alongside the results of the processing operation, would provide exactly-once delivery, but is the most involved in terms of implementation.

## first-time consumers
`auto.offset.reset` setting exists to control where first-time consumers begin reading from (those that don't store offsets in external store)

By default it's set to `latest` , which means consumers will only see messages pushed to the topic after they first read from it. It can also be set to `earliest` to make new consumers play through all messages in the topic, or to `none` to prevent there being any default: an exception is thrown if consumers don't manually seek to an initial offset before first read from a topic.


# commands

```bash
bin/zookeeper-server-start.sh config/zookeeper.properties &
bin/kafka-server-start.sh config/server.properties &
bin/kafka-server-start.sh config/server-1.properties &

bin/kafka-topics.sh --create --zookeeper localhost:2181 --replication-factor 2 --partitions 1 --topic consec-poc
bin/kafka-topics.sh --describe --zookeeper localhost:2181 --topic consec-poc
bin/kafka-topics.sh --list --zookeeper localhost:2181

bin/kafka-console-producer.sh --broker-list localhost:9092,localhost:9093 --topic consec-poc
bin/kafka-console-consumer.sh --bootstrap-server localhost:9092,localhost:9093 --topic consec-poc --from-beginning

# config
bin/kafka-configs.sh --zookeeper localhost:2181 --alter --entity-type topics --entity-name consec-poc --add-config retention.ms=1000
bin/kafka-configs.sh --zookeeper localhost:2181 --alter --entity-type topics --entity-name consec-poc --delete-config retention.ms
bin/kafka-configs.sh --zookeeper localhost:2181 --describe --entity-type topics --entity-name consec-poc
```



##Links

- http://www.cakesolutions.net/teamblogs/getting-started-with-kafka-using-scala-kafka-client-and-akka
- http://kafka.apache.org/documentation/#design

