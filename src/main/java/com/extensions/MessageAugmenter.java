package com.extensions;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * The Message Augmenter essentially provides a placeholder to alter the message while transmitting it from source
 * kafka cluster to destination.
 * In its current form, it replaces the schema id with a new id. This is often helpful when both the clusters work
 * in different schema registry servers.
 */
public class MessageAugmenter {


    private static final byte MAGIC_BYTE = 0x0;
    protected static final int idSize = 4;


    public MessagePayload AugmentSchemaId(byte[] message, int targetSchemaId) throws IOException {
        MessagePayload messagePayload = decode(message);
        return encode(messagePayload, targetSchemaId);
    }

    public ByteBuffer getByteBuffer(byte[] payload) {
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        if (buffer.get() != MAGIC_BYTE)
            throw new IllegalArgumentException("Unknown magic byte!");
        return buffer;
    }

    public byte[] getByteBuffer(byte[] payload, int start, int length) {
        byte[] target = new byte[length];
        System.arraycopy(payload, start, target, 0, length);
        return target;
    }

    public MessagePayload decode(byte[] payload) {
        MessagePayload messagePayload = new MessagePayload();
        ByteBuffer buffer = getByteBuffer(payload);
        messagePayload.sourceSchemaId = Integer.toString(buffer.getInt());
        messagePayload.start = buffer.position() + buffer.arrayOffset();
        messagePayload.length = buffer.limit() - 1 - idSize;
        byte[] bytes = new byte[messagePayload.length];
        System.arraycopy(payload, messagePayload.start, bytes, 0, messagePayload.length);
        messagePayload.dataPayload = bytes;
        return messagePayload;
    }


    public MessagePayload encode(MessagePayload messagePayload, int targetSchemaId) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            out.write(MAGIC_BYTE);
            out.write(ByteBuffer.allocate(4).putInt(targetSchemaId).array());
            out.write(messagePayload.dataPayload);
            messagePayload.augmentedMessage = out.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
        return messagePayload;
    }
}
