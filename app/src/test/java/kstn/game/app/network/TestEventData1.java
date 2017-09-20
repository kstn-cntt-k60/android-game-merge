package kstn.game.app.network;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.event.TestEventType;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TestEventData1 extends BaseEventData {
    private final int id;
    private final String name;

    public TestEventData1(long timeStamp, int id, String name) {
        super(timeStamp);
        this.id = id;
        this.name = name;
    }

    @Override
    public EventType getEventType() {
        return TestEventType.EVENT_TEST1;
    }

    @Override
    public String getName() {
        return "test";
    }

    public String name() {
        return name;
    }

    public int id() {
        return id;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        TestMessage.Test message = TestMessage.Test.newBuilder()
                .setId(id)
                .setName(name)
                .build();
        message.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            TestMessage.Test message = TestMessage.Test.parseDelimitedFrom(in);
            return new TestEventData1(111, message.getId(), message.getName());
        }
    }
}
