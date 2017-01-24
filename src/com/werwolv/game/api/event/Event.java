package com.werwolv.game.api.event;

public abstract class Event {

    private long timeStamp = System.currentTimeMillis();
    private String name;

    public Event(String name) {
        this.name = name;
    }

}
