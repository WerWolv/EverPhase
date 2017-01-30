package com.deltabase.everphase.item;

public class ItemStack {

    private Item item;
    private int stackSize;
    private int maxStackSize;

    public ItemStack(Item item) {
        this.item = item;
        this.stackSize = 1;
        this.maxStackSize = 1;
    }

    public ItemStack(Item item, int stackSize, int maxStackSize) {
        this.item = item;
        this.stackSize = stackSize;
        this.maxStackSize = maxStackSize;
    }

    public boolean decrementStackSize() {
        if(this.stackSize > 1) {
            this.stackSize--;
            return false;
        } else {
            this.stackSize = 0;
            return true;
        }
    }

    public Item getItem() {
        return item;
    }

    public int getStackSize() {
        return stackSize;
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }
}
