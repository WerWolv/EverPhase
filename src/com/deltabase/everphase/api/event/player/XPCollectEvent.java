package com.deltabase.everphase.api.event.player;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.entity.EntityPlayer;

public class XPCollectEvent extends Event {

    private EntityPlayer player;
    private int xpAmount;

    public XPCollectEvent(EntityPlayer player, int xpAmount) {
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
