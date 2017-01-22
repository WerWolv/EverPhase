package com.werwolv.game.item;

import com.werwolv.game.api.event.EventBus;
import com.werwolv.game.api.event.player.PlayerItemUseEvent;
import com.werwolv.game.entity.EntityPlayer;

public class Item {

    private String name;
    private int itemID;
    private int textureID;

    public Item(String name, int itemID) {
        this.name = name;
        this.itemID = itemID;
    }

    public ItemStack onItemClick(ItemStack itemStack, EntityPlayer player, PlayerItemUseEvent.Action action) {
        EventBus.postEvent(new PlayerItemUseEvent(this, player, action));

        return itemStack;
    }

}
