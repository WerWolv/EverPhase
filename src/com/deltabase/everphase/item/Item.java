package com.deltabase.everphase.item;

import com.deltabase.everphase.api.event.EventBus;
import com.deltabase.everphase.api.event.player.PlayerItemUseEvent;
import com.deltabase.everphase.engine.resource.TextureGui;
import com.deltabase.everphase.entity.EntityPlayer;

public class Item {

    private String name;
    private int itemID, metaData;
    private int textureID;

    private int maxStackSize;

    public Item(String name, int itemID, int metaData, TextureGui texture) {
        this.name = name;
        this.itemID = itemID;
        this.metaData = metaData;
        this.textureID = texture.getTextureID();
        this.maxStackSize = 99;
    }

    public ItemStack onItemClick(ItemStack itemStack, EntityPlayer player, PlayerItemUseEvent.Action action) {
        EventBus.postEvent(new PlayerItemUseEvent(this, player, action));

        return itemStack;
    }

    public Item setMaxStackSize(int stackSize) {
        this.maxStackSize = maxStackSize;

        return this;
    }

    public String getName() {
        return name;
    }

    public int getItemID() {
        return itemID;
    }

    public int getMetaData() {
        return metaData;
    }

    public void setMetaData(int metaData) {
        this.metaData = metaData;
    }

    public int getTextureID() {
        return textureID;
    }
}
