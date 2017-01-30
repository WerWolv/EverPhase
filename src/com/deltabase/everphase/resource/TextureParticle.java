package com.deltabase.everphase.resource;

public class TextureParticle {

    private int textureID;
    private int numOfRows;
    private boolean additive;

    public TextureParticle(int textureID, int numOfRows, boolean additive) {
        this.textureID = textureID;
        this.numOfRows = numOfRows;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getNumOfRows() {
        return numOfRows;
    }

    public boolean usesAdditiveBlending() {
        return additive;
    }
}
