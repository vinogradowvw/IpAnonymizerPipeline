package ipanonymizerpipeline;

import java.nio.ByteBuffer;

import org.capnproto.ArrayInputStream;
import org.capnproto.MessageReader;
import org.capnproto.Serialize;

public class CapnpDeserializer {
    
    public static HtmlLog.HttpLogRecord.Reader getDeserializer (ByteBuffer buf) throws java.io.IOException {

        MessageReader message = Serialize.read(new ArrayInputStream(buf));

        HtmlLog.HttpLogRecord.Reader httpLog = message.getRoot(HtmlLog.HttpLogRecord.factory);

        return httpLog;
    }
}
