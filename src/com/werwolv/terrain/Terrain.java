package com.werwolv.terrain;

import com.werwolv.model.ModelRaw;
import com.werwolv.render.ModelLoader;
import com.werwolv.resource.TextureTerrain;
import com.werwolv.resource.TextureTerrainPack;
import com.werwolv.toolbox.ValueNoise;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {

    private static final float SIZE = 800;
    private static final int VERTEX_CNT = 128;
    private static final float MAX_HEIGHT = 40.0F;
    private static final float MIN_HEIGHT = -40.0F;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

    private float x, z;
    private ModelRaw model;
    private TextureTerrainPack texturePack;
    private TextureTerrain blendMap;


    private ValueNoise map = new ValueNoise(800, 800);

    public Terrain(int gridX, int gridZ, ModelLoader loader, TextureTerrainPack texturePack, TextureTerrain blendMap, String heightMap) {
        this.texturePack = texturePack;
        this.blendMap = blendMap;
        this.x = gridX * SIZE;
        this.z = gridZ * SIZE;

        if(heightMap.equals("")) {
            map.calculate();
            this.model = generateTerrain(loader, map.getHeightmap());
        }
        else this.model = generateTerrain(loader, heightMap);
    }

    private ModelRaw generateTerrain(ModelLoader loader, String heightMap){

        BufferedImage image = null;

        try {
            image = ImageIO.read(new File("res/" + heightMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int vertexCnt = image != null ? image.getHeight() : 0;

        int count = vertexCnt * vertexCnt;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (vertexCnt - 1) * (vertexCnt - 1)];
        int vertexPointer = 0;

        for(int i = 0; i < vertexCnt; i++){
            for(int j = 0; j < vertexCnt; j++){
                vertices[vertexPointer * 3] = (float) j /((float) vertexCnt - 1) * SIZE;
                vertices[vertexPointer * 3 + 1] = getHeight(j, i, image);
                vertices[vertexPointer * 3 + 2] = (float) i /((float) vertexCnt - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;                textureCoords[vertexPointer * 2] = (float) j /((float) vertexCnt - 1);
                textureCoords[vertexPointer * 2 + 1] = (float) i /((float) vertexCnt - 1);
                vertexPointer++;
            }
        }

        int pointer = 0;

        for(int gz = 0; gz < vertexCnt-1; gz++){
            for(int gx = 0; gx < vertexCnt - 1; gx++){
                int topLeft = (gz * vertexCnt) + gx;
                int topRight = topLeft + 1;
                int bottomLeft = ((gz + 1) * vertexCnt) + gx;
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
                vertices[vertexPointer * 3 + 1] = heightMap[i][j] * 1000 - heightMap[0][0] * 1000;
                vertices[vertexPointer * 3 + 2] = (float) i /((float) VERTEX_CNT - 1) * SIZE;

                Vector3f normal = calculateNormal(j, i, heightMap);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;

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

    private float getHeight(int x, int z, BufferedImage image) {
        if(x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) return 0;

        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOR / 2.0F;
        height /= MAX_PIXEL_COLOR / 2.0F;
        height *= MAX_HEIGHT;

        return height;
    }

    private float getHeight(int x, int z, float[][] heightmap) {
        if(x < 0 || x >= heightmap.length || z < 0 || z >= heightmap.length) return 0;

        float height = heightmap[x][z];
        height *= MAX_HEIGHT;

        return height;
    }

    private Vector3f calculateNormal(int x, int z, BufferedImage image) {
        float heightL = getHeight(x - 1, z, image);
        float heightR = getHeight(x + 1, z, image);
        float heightD = getHeight(x, z - 1, image);
        float heightU = getHeight(x, z + 1, image);

        Vector3f normal = new Vector3f(heightL - heightR, 2.0F, heightD - heightU);

        normal.normalize();

        return normal;

    }

    private Vector3f calculateNormal(int x, int z, float[][] heightMap) {
        float heightL = getHeight(x - 1, z, heightMap);
        float heightR = getHeight(x + 1, z, heightMap);
        float heightD = getHeight(x, z - 1, heightMap);
        float heightU = getHeight(x, z + 1, heightMap);

        Vector3f normal = new Vector3f(heightL - heightR, 2.0F, heightD - heightU);

        normal.normalize();

        return normal;

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

    public TextureTerrainPack getTexturePack() {
        return texturePack;
    }

    public TextureTerrain getBlendMap() {
        return blendMap;
    }
}
