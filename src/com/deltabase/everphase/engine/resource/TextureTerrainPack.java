package com.deltabase.everphase.engine.resource;

import com.deltabase.everphase.api.EverPhaseApi;

public class TextureTerrainPack {

    private TextureTerrain backgroundTexture;
    private TextureTerrain rTexture;
    private TextureTerrain gTexture;
    private TextureTerrain bTexture;
    private TextureTerrain blendMap;

    public TextureTerrainPack(String backgroundTexture, String rTexture, String gTexture, String bTexture, String blendMap) {
        this.backgroundTexture = new TextureTerrain(EverPhaseApi.RESOURCE_LOADER.loadTexture(backgroundTexture));
        this.rTexture = new TextureTerrain(EverPhaseApi.RESOURCE_LOADER.loadTexture(rTexture));
        this.gTexture = new TextureTerrain(EverPhaseApi.RESOURCE_LOADER.loadTexture(gTexture));
        this.bTexture = new TextureTerrain(EverPhaseApi.RESOURCE_LOADER.loadTexture(bTexture));
        this.blendMap = new TextureTerrain(EverPhaseApi.RESOURCE_LOADER.loadTexture(blendMap));
    }

    public TextureTerrain getBackgroundTexture() {
        return backgroundTexture;
    }

    public TextureTerrain getRedTexture() {
        return rTexture;
    }

    public TextureTerrain getGreenTexture() {
        return gTexture;
    }

    public TextureTerrain getBlueTexture() {
        return bTexture;
    }

    public TextureTerrain getBlendMapTexture() {
        return blendMap;
    }
}
