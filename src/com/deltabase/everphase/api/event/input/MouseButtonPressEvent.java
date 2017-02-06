package com.deltabase.everphase.api.event.input;

import com.deltabase.everphase.api.event.Event;

public class MouseButtonPressEvent extends Event {

    private int pressedBtn;

    public MouseButtonPressEvent(int pressedKey) {
        this.pressedBtn = pressedKey;
    }

    public int getPressedButton() {
        return pressedBtn;
    }
}