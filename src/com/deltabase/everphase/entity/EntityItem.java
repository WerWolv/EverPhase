package com.deltabase.everphase.entity;

import com.deltabase.everphase.item.ItemStack;
import org.joml.Vector3f;

public class EntityItem extends Entity {

    private ItemStack itemStack;

    public EntityItem(ItemStack itemStack) {
        super(0.0F, 1.0F, new Vector3f(1.0F, 1.0F, 1.0F));

        this.itemStack = itemStack;
    }

    public ItemStack getItemStack() {
        return itemStack;
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }
}
