package kstn.game.app.event;

public enum LLBaseEventType implements LLEventType {
    ROOT_RESUME,
    ROOT_PAUSE,

    TCP_SERVER_ACCEPT_ERROR,
    TCP_CONNECTION_ERROR,
    TCP_RECEIVE_DATA,

    UDP_RECEIVE_DATA,

    UI_ADD_LISTENER,
    UI_REMOVE_LISTENER,
    UI_QUEUE_EVENT,

    TOUCH_EVENT,
}
