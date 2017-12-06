package kstn.game.logic.playing_event.answer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class AnswerEvent extends GameEventData {
    private char character;

    public AnswerEvent(char character) {
        super(PlayingEventType.ANSWER);
        this.character = character;
    }

    public char getCharacter() {
        return character;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        AnswerMessage.Answer msg = AnswerMessage.Answer.newBuilder()
                .setCharacter(character)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            AnswerMessage.Answer msg = AnswerMessage.Answer.parseDelimitedFrom(in);
            return new AnswerEvent((char) msg.getCharacter());
        }
    }
}
