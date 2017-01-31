package com.deltabase.everphase.gui.slot;

import com.deltabase.everphase.item.ItemStack;
import org.joml.Vector2f;

public class Slot {

    public static final float SLOT_SIZE = 0.25F;

    private Vector2f position;
    private ItemStack itemStack;
    private int maxStackSize;
    private int slotID;

    public Slot(Vector2f position, int maxStackSize, int slotID) {
        this.position = position;
        this.maxStackSize = maxStackSize;
        this.slotID = slotID;
    }

    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getSlotID() {
        return slotID;
    }
}
