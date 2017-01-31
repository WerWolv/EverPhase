package com.deltabase.everphase.api.event.rendering;

import com.deltabase.everphase.api.event.Event;
import com.deltabase.everphase.item.ItemStack;

public class HoverItemEvent extends Event {

    private ItemStack itemStack;
    private double hoverX, hoverY;

    public HoverItemEvent(ItemStack itemstack, double hoverX, double hoverY) {
        super("HOVERITEMEVENT");

        this.itemStack = itemstack;
        this.hoverX = hoverX;
        this.hoverY = hoverY;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public double getHoverX() {
        return hoverX;
    }

    public double getHoverY() {
        return hoverY;
    }
}
