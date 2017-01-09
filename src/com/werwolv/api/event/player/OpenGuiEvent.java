package com.werwolv.api.event.player;

import com.werwolv.api.event.Event;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.gui.Gui;

public class OpenGuiEvent extends Event {

    private EntityPlayer player;
    private Gui openGui;

    public OpenGuiEvent(EntityPlayer player, Gui openGui) {
        super("PLAYEROPENEDGUIEVENT");

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
