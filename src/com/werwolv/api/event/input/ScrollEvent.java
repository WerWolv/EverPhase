package com.werwolv.api.event.input;

import com.werwolv.api.event.Event;

public class ScrollEvent extends Event {

    private int xScroll, yScroll;

    public ScrollEvent(int xScroll, int yScroll) {
        super("ONSCROLLEVENT");

        this.xScroll = xScroll;
        this.yScroll = yScroll;
    }

    public int getXScroll() {
        return xScroll;
    }

    public int getYScroll() {
        return yScroll;
    }
}
