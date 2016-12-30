package com.werwolv.resource;

import com.werwolv.render.ModelLoader;

public class TextureTerrainPack {

    private TextureTerrain backgroundTexture;
    private TextureTerrain rTexture;
    private TextureTerrain gTexture;
    private TextureTerrain bTexture;
    private TextureTerrain blendMap;

    public TextureTerrainPack(ModelLoader loader, String backgroundTexture, String rTexture, String gTexture, String bTexture, String blendMap) {
        this.backgroundTexture = new TextureTerrain(loader.loadTexture(backgroundTexture));
        this.rTexture = new TextureTerrain(loader.loadTexture(rTexture));
        this.gTexture = new TextureTerrain(loader.loadTexture(gTexture));
        this.bTexture = new TextureTerrain(loader.loadTexture(bTexture));
        this.blendMap = new TextureTerrain(loader.loadTexture(blendMap));
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

    public TextureTerrain getBlendMap() { return blendMap; }
}
