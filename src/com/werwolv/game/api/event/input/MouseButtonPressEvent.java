package com.werwolv.game.api.event.input;

import com.werwolv.game.api.event.Event;

public class MouseButtonPressEvent extends Event {

    private int pressedBtn;

    public MouseButtonPressEvent(int pressedKey) {
        super("MOUSEBUTTONPRESSEVENT");

        this.pressedBtn = pressedKey;
    }

    public int getPressedButton() {
        return pressedBtn;
    }
}