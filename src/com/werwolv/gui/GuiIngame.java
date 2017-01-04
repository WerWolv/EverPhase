package com.werwolv.gui;

import com.werwolv.main.Main;
import com.werwolv.render.RendererMaster;
import org.joml.Vector2f;
import org.joml.Vector4f;

public class GuiIngame extends Gui {

    private GuiTextureUnit textureGuiIngame;

    public GuiIngame(RendererMaster renderer, int texture, Vector2f position, Vector2f scale) {
        super(renderer, texture, position, scale);

        textureGuiIngame = renderer.getModelLoader().loadGuiTexture("gui/guiIngame");
    }

    @Override
    public void render() {
        renderer.getRendererGui().drawTexture((textureGuiIngame.getSize() / Main.getWindowSize()[0]), -1.65F, 0.5F, new Vector4f(0, 230, 256, 256), textureGuiIngame);

        renderer.getRendererGui().drawTexture(-0.5F, 0.2F, 0.5F, new Vector4f(0, 195, 256, 122), textureGuiIngame);

        renderer.getRendererGui().drawTexture(-0.5F, 0.429F, 0.5F, new Vector4f(0, 188, 45, 103), textureGuiIngame);
        renderer.getRendererGui().drawTexture(-0.5F, 0.429F, 0.5F, new Vector4f(0, 175, 40, 100), textureGuiIngame);
        renderer.getRendererGui().drawTexture(-0.5F, 0.429F, 0.5F, new Vector4f(0, 163, 35, 95), textureGuiIngame);

    }
}
