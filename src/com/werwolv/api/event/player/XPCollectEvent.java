package com.werwolv.api.event.player;

import com.werwolv.api.event.Event;
import com.werwolv.entity.EntityPlayer;

public class XPCollectEvent extends Event {

    private EntityPlayer player;
    private int xpAmount;

    public XPCollectEvent(EntityPlayer player, int xpAmount) {
        super("XPCOLLECTEDEVENT");

        this.player = player;
        this.xpAmount = xpAmount;
    }

    public EntityPlayer getPlayer() {
        return player;
    }

    public int getXpAmount() {
        return xpAmount;
    }
}
