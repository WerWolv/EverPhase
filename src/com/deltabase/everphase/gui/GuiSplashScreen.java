package com.deltabase.everphase.gui;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.engine.render.RendererGui;
import com.deltabase.everphase.engine.resource.TextureGui;
import org.joml.Vector4f;

public class GuiSplashScreen extends Gui {


    private TextureGui splashTexture;

    public GuiSplashScreen() {
        splashTexture = EverPhaseApi.RESOURCE_LOADER.loadGuiTexture("gui/guiIngame");
    }

    @Override
    public void render(RendererGui renderer) {
        super.render(renderer);

        renderer.drawTexture(0, 0, 1, new Vector4f(0, 0, 256, 256), splashTexture);
    }
}
