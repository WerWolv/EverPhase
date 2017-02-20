package com.deltabase.everphase.api.event.mp;

import com.deltabase.everphase.api.event.Event;

public class ServerMessageReceivedEvent extends Event {

    private String message;

    public ServerMessageReceivedEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
