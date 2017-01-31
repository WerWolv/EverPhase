package com.deltabase.everphase.inventory;

import com.deltabase.everphase.gui.slot.Slot;
import com.deltabase.everphase.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class Inventory {

    public List<Slot> inventorySlots = new ArrayList<>();

    public abstract int getInventorySize();

    public abstract int getMaxItemStackSize();

    public abstract boolean isItemAllowed(ItemStack itemStack);

}
