package com.deltabase.everphase.gui;

import com.deltabase.everphase.engine.render.RendererGui;
import com.deltabase.everphase.gui.elements.GuiButton;

import java.util.ArrayList;
import java.util.List;

public class Gui {

    public List<GuiButton> buttonList = new ArrayList<>();

    public Gui() {

    }

    public void update() {

    }

    public void render(RendererGui renderer) {
        for (GuiButton button : buttonList)
            renderer.drawTexture(button.getPosition().x, button.getPosition().y, 1.0F, button.getTexturePosition(), button.getTexture());

    }
}
