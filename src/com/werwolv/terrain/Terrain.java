package com.werwolv.terrain;

import com.werwolv.model.ModelRaw;
import com.werwolv.render.ModelLoader;
import com.werwolv.resource.TextureModel;
import com.werwolv.toolbox.ValueNoise;

public class Terrain {

    private static final float SIZE = 800;
    private static final int VERTEX_CNT = 128;

    private float x, z;
    private ModelRaw model;
    private TextureModel texture;
    private ValueNoise map = new ValueNoise(800, 800);

    public Terrain(int gridX, int gridZ, ModelLoader loader, TextureModel texture) {
        this.texture = texture;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;

        map.calculate();

        this.model = generateTerrain(loader, map.getHeightmap());
    }

    private ModelRaw generateTerrain(ModelLoader loader, float[][] heightMap){
        int count = VERTEX_CNT * VERTEX_CNT;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (VERTEX_CNT - 1) * (VERTEX_CNT - 1)];
        int vertexPointer = 0;

        for(int i = 0; i < VERTEX_CNT; i++){
            for(int j = 0; j < VERTEX_CNT; j++){
                vertices[vertexPointer * 3] = (float) j /((float) VERTEX_CNT - 1) * SIZE;
                vertices[vertexPointer * 3 + 1] = heightMap[i][j] * 1000 - 600;
                vertices[vertexPointer * 3 + 2] = (float) i /((float) VERTEX_CNT - 1) * SIZE;
                normals[vertexPointer * 3] = 0;
                normals[vertexPointer * 3 + 1] = 1;
                normals[vertexPointer * 3 + 2] = 0;
                textureCoords[vertexPointer * 2] = (float) j /((float) VERTEX_CNT - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i /((float) VERTEX_CNT - 1);
                vertexPointer++;
            }
        }

        int pointer = 0;

        for(int gz = 0; gz < VERTEX_CNT-1; gz++){
            for(int gx = 0; gx < VERTEX_CNT - 1; gx++){
                int topLeft = (gz * VERTEX_CNT) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * VERTEX_CNT) + gx;
                int bottomRight = bottomLeft + 1;

                indices[pointer++] = topLeft;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = topRight;
                indices[pointer++] = topRight;
                indices[pointer++] = bottomLeft;
                indices[pointer++] = bottomRight;
            }
        }

        return loader.loadToVAO(vertices, textureCoords, normals, indices);
    }

    public float getX() {
        return x;
    }

    public float getZ() {
        return z;
    }

    public ModelRaw getModel() {
        return model;
    }

    public TextureModel getTexture() {
        return texture;
    }
}
