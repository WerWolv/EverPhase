package com.deltabase.everphase.gui.inventory;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.content.Items;
import com.deltabase.everphase.engine.render.RendererGui;
import com.deltabase.everphase.engine.resource.TextureGui;
import com.deltabase.everphase.item.ItemStack;
import org.joml.Vector4f;

public class GuiInventoryPlayer extends GuiInventory {

    private TextureGui textureInventory = EverPhaseApi.RESOURCE_LOADER.loadGuiTexture("gui/guiInventory");

    public GuiInventoryPlayer() {
        super(EverPhaseApi.getEverPhase().thePlayer.getInventoryPlayer());
    }

    @Override
    public void addSlotsToInventory() {

        this.setItemStackInSlot(new ItemStack(Items.itemTest, 1), 0);
        this.setItemStackInSlot(new ItemStack(Items.itemTest2, 1), 1);
    }

    @Override
    public void render(RendererGui renderer) {
        super.render(renderer);
        renderer.drawDefaultBackground();

        renderer.drawTexture(-0.32F, -0.3F, 1.0F, new Vector4f(0, 0, 99, 174), textureInventory);
    }
}
