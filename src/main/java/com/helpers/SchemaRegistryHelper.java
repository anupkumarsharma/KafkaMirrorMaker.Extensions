package com.helpers;

import io.confluent.kafka.schemaregistry.client.CachedSchemaRegistryClient;
import io.confluent.kafka.schemaregistry.client.SchemaMetadata;
import io.confluent.kafka.schemaregistry.client.rest.exceptions.RestClientException;

import java.io.IOException;

public class SchemaRegistryHelper {

    private CachedSchemaRegistryClient client;
    protected int identityMapCapacity = 100;

    public SchemaRegistryHelper(String schemaRegistryUrl) {
        System.out.print("Setting up SchemaRegistryClient For -" + schemaRegistryUrl);
        client = new CachedSchemaRegistryClient(schemaRegistryUrl, identityMapCapacity);
    }

    public int GetSchemaID( String topicName) {
      System.out.print("Getting ID for" + topicName);
        SchemaMetadata metadata = null;
        try {
            metadata = client.getLatestSchemaMetadata(topicName);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return metadata.getId();
    }
}
