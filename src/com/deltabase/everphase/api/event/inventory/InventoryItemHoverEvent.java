package com.deltabase.everphase.api.event.inventory;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.gui.inventory.GuiInventory;
import com.deltabase.everphase.gui.slot.Slot;
import com.deltabase.everphase.item.ItemStack;

public class InventoryItemHoverEvent extends Event {

    private ItemStack itemStack;
    private Slot slot;
    private double hoverX, hoverY;
    private GuiInventory gui;

    public InventoryItemHoverEvent(ItemStack itemstack, Slot slot, double hoverX, double hoverY) {
        super("HOVERITEMEVENT");

        this.gui = gui;
        this.itemStack = itemstack;
        this.slot = slot;
        this.hoverX = hoverX;
        this.hoverY = hoverY;
    }

    public GuiInventory getGui() {
        return gui;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Slot getSlot() {
        return slot;
    }

    public double getHoverX() {
        return hoverX;
    }

    public double getHoverY() {
        return hoverY;
    }
}
