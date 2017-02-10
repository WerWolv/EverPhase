package com.deltabase.everphase.inventory;

import com.deltabase.everphase.gui.slot.Slot;
import org.joml.Vector2f;

import java.util.List;

public class InventoryPlayer extends Inventory {

    private int inventorySize;
    private int maxStackSize;

    public InventoryPlayer(int inventorySize, int maxStackSize) {
        this.inventorySize = inventorySize;
        this.maxStackSize = maxStackSize;


        for (int i = 0; i < 4; i++)
            for (int j = 0; j < 9; j++)
                this.addSlot(new Slot(this, new Vector2f(-0.924F + (0.13F) * i, 0.605F - (0.13F) * j), 99));

    }

    @Override
    public int getInventorySize() {
        return getInventorySlots().size();
    }

    @Override
    public int getMaxItemStackSize() {
        return maxStackSize;
    }

    public List<Slot> getHotbarSlots() {
        return this.getInventorySlots().subList(27, 35);
    }

    //TODO: Add functions!!
    // readFromNBT
    // writeToNBT
}
