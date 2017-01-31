package com.deltabase.everphase.inventory;

import com.deltabase.everphase.gui.slot.Slot;
import com.deltabase.everphase.item.ItemStack;
import org.joml.Vector2f;

public class InventoryPlayer extends Inventory {

    private int inventorySize;
    private int maxStackSize;

    public InventoryPlayer(int inventorySize, int maxStackSize) {
        this.inventorySize = inventorySize;
        this.maxStackSize = maxStackSize;

        inventorySlots.add(new Slot(new Vector2f(0.0F, 0.0F), 99, 0));
    }

    @Override
    public int getInventorySize() {
        return inventorySlots.size();
    }

    @Override
    public int getMaxItemStackSize() {
        return maxStackSize;
    }

    @Override
    public boolean isItemAllowed(ItemStack itemStack) {
        return true;
    }

    //TODO: Add functions!!
    // readFromNBT
    // writeToNBT
}
