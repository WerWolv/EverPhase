package com.werwolv.game.api.event.player;

import com.werwolv.game.api.event.Event;
import com.werwolv.game.entity.EntityPlayer;
import com.werwolv.game.gui.Gui;

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
