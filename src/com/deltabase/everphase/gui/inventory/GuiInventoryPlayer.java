package com.deltabase.everphase.gui.inventory;

import com.deltabase.everphase.engine.render.RendererMaster;
import com.deltabase.everphase.inventory.InventoryPlayer;
import com.deltabase.everphase.item.ItemStack;
import com.deltabase.everphase.item.Items;

public class GuiInventoryPlayer extends GuiInventory {

    public GuiInventoryPlayer(RendererMaster renderer) {
        super(renderer, new InventoryPlayer(1, 99));

        this.addItemStack(new ItemStack(Items.itemTest, 1));
    }
}
