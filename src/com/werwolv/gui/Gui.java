package com.werwolv.gui;

import org.joml.Vector2f;

public class Gui {

    private int texture;            //The texture of the GUI
    private Vector2f position;      //The position of the GUI on the screen
    private Vector2f scale;         ///The size of the GUI

    public Gui(int texture, Vector2f position, Vector2f scale) {
        this.texture = texture;
        this.position = position;
        this.scale = scale;
    }

    /* Getters */

    public int getTexture() {
        return texture;
    }

    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }
}
