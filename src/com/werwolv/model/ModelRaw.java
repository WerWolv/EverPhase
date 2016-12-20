package com.werwolv.model;

public class ModelRaw {

    private int vaoID;
    private int vertexCnt;

    public ModelRaw(int vaoID, int vertexCnt) {
        this.vaoID = vaoID;
        this.vertexCnt = vertexCnt;
    }

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCnt() {
        return vertexCnt;
    }

}
