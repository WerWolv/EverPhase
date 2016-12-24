package com.werwolv.resource;

public class TextureTerrainPack {

    private TextureTerrain backgroundTexture;
    private TextureTerrain rTexture;
    private TextureTerrain gTexture;
    private TextureTerrain bTexture;

    public TextureTerrainPack(TextureTerrain backgroundTexture, TextureTerrain rTexture, TextureTerrain gTexture, TextureTerrain bTexture) {
        this.backgroundTexture = backgroundTexture;
        this.rTexture = rTexture;
        this.gTexture = gTexture;
        this.bTexture = bTexture;
    }

    public TextureTerrain getBackgroundTexture() {
        return backgroundTexture;
    }

    public TextureTerrain getrTexture() {
        return rTexture;
    }

    public TextureTerrain getgTexture() {
        return gTexture;
    }

    public TextureTerrain getbTexture() {
        return bTexture;
    }
}
