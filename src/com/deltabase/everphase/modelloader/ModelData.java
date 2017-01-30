package com.deltabase.everphase.modelloader;

public class ModelData {

    private float[] vertices;       //The three points to create a triangle
    private float[] textureCoords;  //The coordinates on the texture that should get rendered on the fragment
    private float[] normals;        //The vectors of a vertex that points 90° away from it
    private int[] indices;          //The order in which the vertices should get used to draw triangles
    private float furthestPoint;    //The point furthest away on the model

    public ModelData(float[] vertices, float[] textureCoords, float[] normals, int[] indices,
                     float furthestPoint) {
        this.vertices = vertices;
        this.textureCoords = textureCoords;
        this.normals = normals;
        this.indices = indices;
        this.furthestPoint = furthestPoint;
    }

    /* Getters and Setters */

    public float[] getVertices() {
        return vertices;
    }

    public float[] getTextureCoords() {
        return textureCoords;
    }

    public float[] getNormals() {
        return normals;
    }

    public int[] getIndices() {
        return indices;
    }

    public float getFurthestPoint() {
        return furthestPoint;
    }

}
