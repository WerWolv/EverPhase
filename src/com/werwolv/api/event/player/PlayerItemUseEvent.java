package com.werwolv.api.event.player;

import com.werwolv.api.event.Event;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.item.Item;

public class PlayerItemUseEvent extends Event {

    private Item item;
    private EntityPlayer player;
    private Action useAction;

    public enum Action {
        LEFT_CLICK,
        RIGHT_CLICK,
        SHIFT_LEFT_CLICK,
        SHIFT_RIGHT_CLICK,
        CTRL_LEFT_CLICK,
        CTRL_RIGHT_CLICK,
        NONE
    }

    public PlayerItemUseEvent(Item item, EntityPlayer player, Action useAction) {
        super("PLAYERITEMUSEEVENT");

        this.item = item;
        this.player = player;
        this.useAction = useAction;
    }

}
