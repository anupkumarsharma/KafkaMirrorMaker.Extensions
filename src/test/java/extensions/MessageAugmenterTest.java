package extensions;

import com.extensions.MessageAugmenter;
import org.junit.Assert;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

public class MessageAugmenterTest {
    private static final byte MAGIC_BYTE = 0x0;

    private ByteBuffer getByteBuffer(byte[] payload) {
        ByteBuffer buffer = ByteBuffer.wrap(payload);
        if (buffer.get() != MAGIC_BYTE)
            throw new IllegalArgumentException("Unknown magic byte!");
        return buffer;
    }

    @Test
    public void ShouldBeAbleToSwitchTheSchemaIdWithPayloadIntact() throws IOException {

        String p = "This is the message";
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        out.write(MAGIC_BYTE);
        out.write(ByteBuffer.allocate(4).putInt(20).array());
        out.write(p.getBytes());
        MessageAugmenter messageAugmenter = new MessageAugmenter();
        byte[] augmentedMessage = messageAugmenter.AugmentSchemaId(out.toByteArray(), 9).augmentedMessage;
        ByteBuffer buffer = getByteBuffer(augmentedMessage);
        String sourceSchemaId = Integer.toString(buffer.getInt());
        int start = buffer.position() + buffer.arrayOffset();
        int length = buffer.limit() - 5;
        byte[] dest = messageAugmenter.getByteBuffer(augmentedMessage, start, length);
        String doc = new String(dest, "ISO-8859-1");
        Assert.assertEquals(doc, p);
        Assert.assertEquals(sourceSchemaId, "9");

    }

}