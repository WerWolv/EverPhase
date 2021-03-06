package com.deltabase.everphase.engine.resource;

public class TextureModel {

    private int textureID;
    private int normalMapID;
    private int extraInfoMapID;

    private float shineDamper = 1;
    private float reflectivity = 0;

    private boolean hasTransparency = false;
    private boolean useFakeLightning = false;
    private boolean hasExtraInfoMap = false;

    private int numOfRows = 1;

    public TextureModel(int textureID) {
        this.textureID = textureID;
    }

    public int getTextureID() {
        return textureID;
    }

    public int getNormalMapID() {
        return normalMapID;
    }

    public void setNormalMapID(int normalMapID) {
        this.normalMapID = normalMapID;
    }

    public int getExtraInfoMapID() {
        return extraInfoMapID;
    }

    public void setExtraInfoMapID(int extraInfoMapID) {
        this.extraInfoMapID = extraInfoMapID;
        this.hasExtraInfoMap = true;
    }

    public boolean hasExtraInfoMap() {
        return hasExtraInfoMap;
    }

    public float getShineDamper() {
        return shineDamper;
    }

    public void setShineDamper(float shineDamper) {
        this.shineDamper = shineDamper;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public void setReflectivity(float reflectivity) {
        this.reflectivity = reflectivity;
    }


    public boolean hasTransparency() {
        return hasTransparency;
    }

    public void setHasTransparency(boolean hasTransparency) {
        this.hasTransparency = hasTransparency;
    }

    public boolean useFakeLightning() {
        return useFakeLightning;
    }

    public void UseFakeLightning(boolean useFakeLightning) {
        this.useFakeLightning = useFakeLightning;
    }


    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }
}
