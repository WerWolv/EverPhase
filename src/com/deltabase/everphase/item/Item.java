package com.deltabase.everphase.item;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.api.event.player.PlayerItemUseEvent;
import com.deltabase.everphase.entity.EntityPlayer;

public class Item {

    private String unlocalizedName;
    private int itemID, metaData;
    private int textureID;

    private int maxStackSize;

    public Item(int itemID, int metaData, String texturePath) {
        this.unlocalizedName = "item.null.name";
        this.itemID = itemID;
        this.metaData = metaData;
        this.textureID = EverPhaseApi.RESOURCE_LOADER.loadGuiTexture(texturePath).getTextureID();
        this.maxStackSize = 99;
    }

    public ItemStack onItemClick(ItemStack itemStack, EntityPlayer player, PlayerItemUseEvent.Action action) {
        EverPhaseApi.EVENT_BUS.postEvent(new PlayerItemUseEvent(this, player, action));

        return itemStack;
    }

    public Item setMaxStackSize(int maxStackSize) {
        this.maxStackSize = maxStackSize;

        return this;
    }

    public Item setUnlocalizedName(String unlocalizedName) {
        this.unlocalizedName = unlocalizedName;

        return this;
    }

    public String getTooltipDescription() {
        return "Hello World";
    }

    public String getName() {
        return unlocalizedName;
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
