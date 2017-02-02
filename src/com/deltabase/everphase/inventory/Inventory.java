package com.deltabase.everphase.inventory;

import com.deltabase.everphase.gui.slot.Slot;
import com.deltabase.everphase.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Inventory {

    private int slotPointer = 0;
    private List<Slot> inventorySlots = new ArrayList<>();
    private ItemStack pickedUpItemStack = null;

    public abstract int getInventorySize();

    public abstract int getMaxItemStackSize();

    public abstract boolean isItemAllowed(ItemStack itemStack);

    public void addSlot(Slot slot) {
        slot.setSlotID(slotPointer);
        inventorySlots.add(slotPointer, slot);
        slotPointer++;
    }


    public ItemStack getPickedUpItemStack() {
        return pickedUpItemStack;
    }

    public void setPickedUpItemStack(ItemStack pickedUpItemStack) {
        this.pickedUpItemStack = pickedUpItemStack;
    }

    public List<Slot> getInventorySlots() {
        return inventorySlots;
    }

    public void setItemStackInSlot(ItemStack itemStack, int slotId) {
        int index = 0;
        for (Slot slot : inventorySlots) {
            if (slot.getSlotID() == slotId) {
                slot.setItemStack(itemStack);
                this.inventorySlots.set(index, slot);
            }
            index++;
        }
    }

}
