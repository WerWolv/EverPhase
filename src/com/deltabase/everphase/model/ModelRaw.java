package com.deltabase.everphase.model;

public class ModelRaw {

    private int vaoID;      //The ID of the Vertex Array Object
    private int vertexCnt;  //The amount of vertices of the model

    public ModelRaw(int vaoID, int vertexCnt) {
        this.vaoID = vaoID;
        this.vertexCnt = vertexCnt;
    }

    /* Getters */

    public int getVaoID() {
        return vaoID;
    }

    public int getVertexCnt() {
        return vertexCnt;
    }

}
