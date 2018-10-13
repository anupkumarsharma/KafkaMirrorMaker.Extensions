KafkaMirrorMaker.Extensions
======================================

This allows to create multiple extensions to manipulate the payloads as they are copied from source Kafka cluster to target cluster.

Kafka Mirror is preferred for end to end kafka replication without heavy serdes requirement. This extension allows to alter the schema id 
based on the schema registry server of target destination.

* Logic - It takes the message from Kafka uses the wire format to read the bytes. It then reads the schema id from schema registry server for the target topic.
The target schema id is then stuffed into message and sent to destination.  

The argument need to be passed is like - registry,http://schema-registry.com/;topic1,topic1. This specifies the target schema registry server url and topic1:topic1 = sourcetopicname:destinationtopicname

* Update the classpath 

    `export CLASSPATH=/etc/kafka-mirror-maker/KafkaMirrorMaker.Extensions-1.0-SNAPSHOT-jar-with-dependencies.jar`

* Start MirrorMaker. Specify the Handler class in "--message.handler" and any arguments in "--message.handler.args". For example:

    `/usr/hdp/current/kafka-broker/bin/kafka-mirror-maker.sh --whitelist topic1  --abort.on.send.failure true --new.consumer --producer.config /tmp/mirror-maker/producer.config --consumer.config /tmp/mirror-maker/consumer.config --message.handler com.extensions.SchemaRegistryIdSwitcher --message.handler.args "registry,http://schema-registry.com/;topic1,topic1`


* Recommended docker image which can work seamlessly - https://hub.docker.com/r/ambuds/mirror-maker/

* Thanks for other authors who has similar extensions - https://github.com/opencore/mirrormaker_topic_rename

* To Build 
     `mvn clean; mvn build; mvn package`