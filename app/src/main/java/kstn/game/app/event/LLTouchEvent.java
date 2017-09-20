package kstn.game.app.event;

import kstn.game.view.screen.View;

// immutable
public class LLTouchEvent extends LLBaseEventData implements View.TouchEvent {
    private final int type;
    private final float x, y;

    public LLTouchEvent(int type, float x, float y) {
        super(LLBaseEventType.TOUCH_EVENT);
        this.type = type;
        this.x = x;
        this.y = y;
    }

    @Override
    public int getType() {
        return type;
    }

    @Override
    public float getX() {
        return x;
    }

    @Override
    public float getY() {
        return y;
    }

    @Override
    public View.TouchEvent setXY(float x, float y) {
        return new LLTouchEvent(type, x, y);
    }
}
