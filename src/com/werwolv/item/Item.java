package com.werwolv.item;

import com.werwolv.api.event.EventBus;
import com.werwolv.api.event.player.PlayerItemUseEvent;
import com.werwolv.entity.EntityPlayer;

public class Item {

    private String name;
    private int itemID;
    private int textureID;

    public Item(String name, int itemID) {
        this.name = name;
        this.itemID = itemID;
    }

    public ItemStack onItemClick(ItemStack item, EntityPlayer player, PlayerItemUseEvent.Action action) {
        EventBus.postEvent(new PlayerItemUseEvent(this, player, action));

        return player.getHeldItem();
    }

}
