package com.werwolv.game.api.event.input;

import com.werwolv.game.api.event.Event;

public class KeyPressEvent extends Event {

    private int pressedKey;

    public KeyPressEvent(int pressedKey) {
        super("KEYPRESSEVENT");

        this.pressedKey = pressedKey;
    }

    public int getPressedKey() {
        return pressedKey;
    }
}
