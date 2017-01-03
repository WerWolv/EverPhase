package com.werwolv.gui;

import com.werwolv.render.RendererMaster;
import org.joml.Vector2f;

public class GuiInventory extends Gui {

    private int textureID;

    public GuiInventory(RendererMaster renderer, int texture, Vector2f position, Vector2f scale) {
        super(renderer, texture, position, scale);

        textureID = renderer.getModelLoader().loadTexture("crate");
    }

    @Override
    public void render() {
        renderer.getRendererGui().drawTexture(0.5F, 0.5F, 0.1F, textureID);
        renderer.getRendererGui().drawTexture(0.7F, 0.3F, 0.2F, textureID);
    }
}
