package com.werwolv.game.gui;

import com.werwolv.game.render.RendererMaster;
import org.joml.Vector2f;

public class Gui {

    protected int texture;            //The texture of the GUI
    protected Vector2f position;      //The position of the GUI on the screen
    protected Vector2f scale;         ///The size of the GUI
    protected RendererMaster renderer;

    public Gui(RendererMaster renderer, int texture, Vector2f position, Vector2f scale) {
        this.renderer = renderer;
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    public void render() {

    }

    /* Getters */

    public int getTexture() {
        return texture;
    }

    public void setTexture(int texture) { this.texture = texture; }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }
}
