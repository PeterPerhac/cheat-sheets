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

"leader" is the node responsible for all reads and writes for the given partition. Each node will be the leader for a randomly selected portion of the partitions.
"replicas" is the list of nodes that replicate the log for this partition regardless of whether they are the leader or even if they are currently alive.
"isr" is the set of "in-sync" replicas. This is the subset of the replicas list that is currently alive and caught-up to the leader.


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

for the producer: --broker-list and --topic are REQUIRED arguments
for the consumer: --bootstrap-server or --zookeeper is a required argument


Kafka can't be categorised as providing publish-subscribe XOR typical message queue semantics (where publish-subscribe means every subscriber receives a copy of a given message, while "typical message queue semantics" means each message is delivered to only one recipient).

When a consumer subscribes to a topic it specifies a consumer group to subscribe as. The existence of a consumer group is implied by using its name during subscription; no explicit creation is required.

A consumer group can also be thought of as as "logical subscriber" in publish-subscribe terminology.

Kafka does require ZooKeeper. It's not an option.
Kafka uses ZooKeeper for basically ALL THE METADATA, with topic offsets being one notable exception.


commands

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

