package com.deltabase.everphase.api.event.input;

import com.deltabase.everphase.api.event.Event;

public class ScrollEvent extends Event {

    private int xScroll, yScroll;

    public ScrollEvent(int xScroll, int yScroll) {
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
