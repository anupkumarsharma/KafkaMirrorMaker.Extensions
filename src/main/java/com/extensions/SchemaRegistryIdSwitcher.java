package com.extensions;

import com.helpers.SchemaRegistryHelper;
import kafka.consumer.BaseConsumerRecord;
import kafka.tools.MirrorMaker;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SchemaRegistryIdSwitcher implements MirrorMaker.MirrorMakerMessageHandler {

    private MessageAugmenter messageAugmenter;
    SchemaRegistryHelper schemaRegistryHelper;

    public Map<String, String> argumentMap;

    public SchemaRegistryIdSwitcher(String argument) {
        this.parseArgument(argument);
        messageAugmenter = new MessageAugmenter();
        if (!argumentMap.containsKey("registry")) {
            throw new IllegalArgumentException("Required Parameter: registry Missing");
        }
        schemaRegistryHelper = new SchemaRegistryHelper(argumentMap.get("registry"));
    }


    public void parseArgument(String argList) {
        argumentMap = new HashMap<String, String>();
        try {
            String[] argumentList = argList.split(";");
            for (String s : argumentList) {
                String[] value = s.split(",");
                argumentMap.put(value[0], value[1]);
            }
        } catch (Exception ex) {
            throw new IllegalArgumentException("Required Parameter: registry Missing");
        }
    }

    public List<ProducerRecord<byte[], byte[]>> handle(BaseConsumerRecord record) {

        long startTime = System.currentTimeMillis();
        try {
            // If the augmentMap contains the topic, then get the new schema id
            if (argumentMap.containsKey(record.topic())) {
                System.out.print("Augmented Message for topic: " + record.topic());
                //
                int revisedSchemaId = schemaRegistryHelper.GetSchemaID(argumentMap.get(record.topic()));
                byte[] augmentedMessage = messageAugmenter.AugmentSchemaId(record.value(), revisedSchemaId).augmentedMessage;
                long elapsedTime  = System.currentTimeMillis() - startTime;
                System.out.println("Total Time Taken - "+ elapsedTime);
                return Collections.singletonList(new ProducerRecord<byte[], byte[]>(
                        record.topic(),
                        null,
                        record.key(),
                        augmentedMessage));
            }
        } catch (IOException e) {
            System.err.print(e.toString());

        }
        return Collections.singletonList(new ProducerRecord<byte[], byte[]>(
                record.topic(),
                null,
                record.key(),
                record.value()));
    }
}
