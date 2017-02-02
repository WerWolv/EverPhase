package com.deltabase.everphase.api.event.inventory;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.gui.slot.Slot;
import com.deltabase.everphase.inventory.Inventory;
import com.deltabase.everphase.item.ItemStack;

public class InventoryItemClickEvent extends Event {

    public static final int MOUSE_BUTTON_LEFT = 0;
    public static final int MOUSE_BUTTON_MIDDLE = 1;
    public static final int MOUSE_BUTTON_RIGHT = 2;

    private ItemStack itemStack;
    private Slot slot;
    private int pressedButton = -1;
    private Inventory inventory;

    public InventoryItemClickEvent(Inventory inventory, ItemStack itemstack, Slot slot, int pressedButton) {
        super("INVENTORYITEMCLICKEVENT");

        this.inventory = inventory;
        this.itemStack = itemstack;
        this.slot = slot;
        this.pressedButton = pressedButton;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public int getPressedButton() {
        return pressedButton;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public Slot getSlot() {
        return slot;
    }
}
