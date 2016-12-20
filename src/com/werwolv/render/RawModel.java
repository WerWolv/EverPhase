package com.werwolv.render;

public class RawModel {

    private int vaoID;
    private int vertexCnt;

    public RawModel(int vaoID, int vertexCnt) {
        this.vaoID = vaoID;
        this.vertexCnt = vertexCnt;
    }

    public int getVaoID() {
        return vaoID;
    }

    public void setVaoID(int vaoID) {
        this.vaoID = vaoID;
    }

    public int getVertexCnt() {
        return vertexCnt;
    }

    public void setVertexCnt(int vertexCnt) {
        this.vertexCnt = vertexCnt;
    }

}
