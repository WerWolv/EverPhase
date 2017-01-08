package com.werwolv.api.event;

public abstract class Event {

    protected long timeStamp = System.currentTimeMillis();
    protected String name;

    public Event(String name) {
        this.name = name;
    }

}
