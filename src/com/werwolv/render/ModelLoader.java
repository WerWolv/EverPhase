package com.werwolv.render;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();

    public RawModel loadToVAO(float[] positions, int[] indices) {
        int vaoID = createVAO();
        bindIndicesBuffer(indices);
        storeDataInAttrList(0, positions);
        unbindVAO();

        return new RawModel(vaoID, indices.length);
    }

    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();
        vaos.add(vaoID);
        GL30.glBindVertexArray(vaoID);

        return vaoID;
    }

    private void  storeDataInAttrList(int attrNr, float[] data) {
        int vboID = GL15.glGenBuffers();

        vbos.add(vboID);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, storeDataInFloatBuffer(data), GL15.GL_STATIC_DRAW);

        GL20.glVertexAttribPointer(attrNr, 3, GL11.GL_FLOAT, false, 0, 0);
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);
    }

    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();
        vbos.add(vboID);

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, storeDataInIntBuffer(indices), GL15.GL_STATIC_DRAW);
    }

    public void clean() {
        for(int vao : vaos)
            GL30.glDeleteVertexArrays(vao);

        for(int vbo : vbos)
            GL15.glDeleteBuffers(vbo);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        return (FloatBuffer) BufferUtils.createFloatBuffer(data.length).put(data).flip();
    }

    private IntBuffer storeDataInIntBuffer(int[] data) {
        return (IntBuffer) BufferUtils.createIntBuffer(data.length).put(data).flip();
    }}
