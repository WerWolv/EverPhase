package com.deltabase.everphase.api.event.input;

import com.deltabase.everphase.api.event.Event;

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
