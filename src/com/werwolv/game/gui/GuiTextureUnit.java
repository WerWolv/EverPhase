package com.werwolv.game.gui;

public class GuiTextureUnit {

    private float posX, posY;
    private float scale;
    private int size;
    private int textureID;

    public GuiTextureUnit(float posX, float posY, float scale, int size, int textureID) {
        this.posX = posX;
        this.posY = posY;
        this.scale = scale;
        this.size = size;
        this.textureID = textureID;
    }

    public float getPosX() {
        return posX;
    }

    public float getPosY() {
        return posY;
    }

    public float getScale() {
        return scale;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getSize() {
        return size;
    }
}
