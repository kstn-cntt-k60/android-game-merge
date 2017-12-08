package kstn.game.logic.playing_event;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.player.NextPlayerEvent;

public class NextQuestionEvent extends GameEventData {
    private final String question;
    private final String answer;

    public NextQuestionEvent(String question, String answer) {
        super(PlayingEventType.NEXT_QUESTION);
        this.question = question;
        this.answer = answer;
    }
    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public void serialize(OutputStream out) throws IOException {
        PlayingMessage.NextQuestion msg = PlayingMessage.NextQuestion.newBuilder()
                .setQuestion(question)
                .setAnswer(answer)
                .build();
        msg.writeDelimitedTo(out);
    }

    public static class Parser implements EventData.Parser {
        @Override
        public EventData parseFrom(InputStream in) throws IOException {
            PlayingMessage.NextQuestion msg = PlayingMessage.NextQuestion.parseDelimitedFrom(in);
            return new NextQuestionEvent(msg.getQuestion(), msg.getAnswer());
        }
    }
}
