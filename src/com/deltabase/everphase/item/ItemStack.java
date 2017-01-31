package com.deltabase.everphase.item;

public class ItemStack {

    private Item item;
    private int stackSize;

    public ItemStack(Item item) {
        this.item = item;
        this.stackSize = 1;
    }

    public ItemStack(Item item, int stackSize) {
        this.item = item;
        this.stackSize = stackSize;
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

    public void setItem(Item item) {
        this.item = item;
    }

    public int getStackSize() {
        return stackSize;
    }

    public void setStackSize(int stackSize) {
        this.stackSize = stackSize;
    }
}
