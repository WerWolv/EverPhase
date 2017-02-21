package com.deltabase.everphase.gui;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.engine.render.RendererGui;
import com.deltabase.everphase.engine.resource.TextureGui;
import com.deltabase.everphase.gui.elements.GuiButton;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class GuiMainMenu extends Gui {

    private TextureGui splashTexture;

    public GuiMainMenu() {
        splashTexture = EverPhaseApi.RESOURCE_LOADER.loadGuiTexture("gui/guiIngame");

        buttonList.add(new GuiButton(0, new Vector2f(0, 0), new Vector4f(0, 0, 50, 50), "", splashTexture));
    }

    @Override
    public void render(RendererGui renderer) {
        super.render(renderer);

        renderer.drawTexture(0, 0, 1, new Vector4f(0, 0, 256, 256), splashTexture);
    }
}
