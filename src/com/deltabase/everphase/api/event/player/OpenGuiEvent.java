package com.deltabase.everphase.api.event.player;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.gui.Gui;

public class OpenGuiEvent extends Event {

    private EntityPlayer player;
    private Gui openGui;

    public OpenGuiEvent(EntityPlayer player, Gui openGui) {
        this.player = player;
        this.openGui = openGui;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public Gui getOpenGui() {
        return openGui;
    }
}
