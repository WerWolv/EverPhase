package com.deltabase.everphase.gui.inventory;

import com.deltabase.everphase.gui.Gui;
import com.deltabase.everphase.gui.slot.Slot;
import com.deltabase.everphase.inventory.Inventory;
import com.deltabase.everphase.item.ItemStack;

public abstract class GuiInventory extends Gui {

    private Inventory inventory;

    private int inventorySize, maxStackSize;

    public GuiInventory() {

    }

    public ItemStack[] getInventoryItemStacks() {
        ItemStack[] items = new ItemStack[inventorySize];

        for (int slot = 0; slot < inventorySize; slot++) {
            items[slot] = inventory.getInventorySlots().get(slot).getItemStack();
        }

        return items;
    }

    public ItemStack getItemStackInSlot(int index) {
        if (index > inventorySize - 1) return null;

        return getInventoryItemStacks()[index];
    }

    public void addItemStackToSlot(ItemStack itemStack, int index) {
        if (getItemStackInSlot(index).getItem() == itemStack.getItem())
            getItemStackInSlot(index).setStackSize(itemStack.getStackSize() + getItemStackInSlot(index).getStackSize());
        else getInventoryItemStacks()[index] = itemStack;
    }

    public void addItemStack(ItemStack itemStack) {
        for (Slot slot : inventory.getInventorySlots())
            if (slot.getItemStack() == null)
                slot.setItemStack(itemStack);
    }

    public void setItemStackInSlot(ItemStack itemStack, int index) {
        inventory.setItemStackInSlot(itemStack, index);
    }

    public void removeItemStack(ItemStack itemStack) {
        for (int slot = 0; slot < inventorySize - 1; slot++)
            if (getItemStackInSlot(slot).getItem() == itemStack.getItem())
                getItemStackInSlot(slot).setStackSize(getInventoryItemStacks()[slot].getStackSize() - itemStack.getStackSize());
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
        this.inventorySize = inventory.getInventorySize();
        this.maxStackSize = inventory.getMaxItemStackSize();

        this.addSlotsToInventory();
    }

    public void addSlotsToInventory() {

    }

}
