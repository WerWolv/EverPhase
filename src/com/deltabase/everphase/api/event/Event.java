package com.deltabase.everphase.api.event;

public abstract class Event {

    private long timeStamp = System.currentTimeMillis();

    public Event() {

    }

    public long getTimeStamp() {
        return timeStamp;
    }
}
