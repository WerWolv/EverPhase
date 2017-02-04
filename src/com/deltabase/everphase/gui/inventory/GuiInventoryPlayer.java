package com.deltabase.everphase.gui.inventory;

import com.deltabase.everphase.content.Items;
import com.deltabase.everphase.engine.render.RendererMaster;
import com.deltabase.everphase.inventory.InventoryPlayer;
import com.deltabase.everphase.item.ItemStack;

public class GuiInventoryPlayer extends GuiInventory {

    public GuiInventoryPlayer(RendererMaster renderer) {
        super(renderer, new InventoryPlayer(1, 99));

        this.setItemStackInSlot(new ItemStack(Items.itemTest, 1), 0);
        this.setItemStackInSlot(new ItemStack(Items.itemTest2, 1), 1);
    }
}
