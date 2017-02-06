package com.deltabase.everphase.api.event.player;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.item.Item;

public class PlayerItemUseEvent extends Event {

    private Item item;
    private EntityPlayer player;
    private Action useAction;

    public PlayerItemUseEvent(Item item, EntityPlayer player, Action useAction) {
        this.item = item;
        this.player = player;
        this.useAction = useAction;
    }

    public enum Action {
        LEFT_CLICK,
        RIGHT_CLICK,
        SHIFT_LEFT_CLICK,
        SHIFT_RIGHT_CLICK,
        CTRL_LEFT_CLICK,
        CTRL_RIGHT_CLICK,
        NONE
    }

}
