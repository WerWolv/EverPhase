package com.deltabase.everphase.inventory;

import com.deltabase.everphase.gui.slot.Slot;
import org.joml.Vector2f;

public class InventoryPlayer extends Inventory {

    private int inventorySize;
    private int maxStackSize;

    public InventoryPlayer(int inventorySize, int maxStackSize) {
        this.inventorySize = inventorySize;
        this.maxStackSize = maxStackSize;


        this.addSlot(new Slot(this, new Vector2f(0.5F, 0.5F), 99));
        this.addSlot(new Slot(this, new Vector2f(0.35F, 0.5F), 99));
        this.addSlot(new Slot(this, new Vector2f(0.20F, 0.5F), 99));

    }

    @Override
    public int getInventorySize() {
        return getInventorySlots().size();
    }

    @Override
    public int getMaxItemStackSize() {
        return maxStackSize;
    }

    //TODO: Add functions!!
    // readFromNBT
    // writeToNBT
}
