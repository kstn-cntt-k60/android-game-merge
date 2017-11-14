package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 14/11/2017.
 */

public class TypedCharacterData extends BaseEventData {
    private final int indexTyped;
    private final int indexShow;
    public TypedCharacterData(int indexTyped, int indexShow) {
        super(0);
        this.indexShow = indexShow;
        this.indexTyped = indexTyped;
    }

    public int getIndexTyped() {
        return indexTyped;
    }

    public int getIndexShow() {
        return indexShow;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.TYPED_CHARACTER;
    }

    @Override
    public String getName() {
        return null;
    }
}
