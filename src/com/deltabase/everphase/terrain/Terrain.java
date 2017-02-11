package com.deltabase.everphase.terrain;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.engine.model.ModelRaw;
import com.deltabase.everphase.engine.modelloader.ModelData;
import com.deltabase.everphase.engine.resource.TextureTerrain;
import com.deltabase.everphase.engine.resource.TextureTerrainPack;
import com.deltabase.everphase.engine.toolbox.Maths;
import org.joml.Vector2f;
import org.joml.Vector3f;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Terrain {

    public static final float SIZE = 512;
    private static final int VERTEX_CNT = 64;
    private static final float MAX_HEIGHT = 40.0F;
    private static final float MIN_HEIGHT = -40.0F;
    private static final float MAX_PIXEL_COLOR = 256 * 256 * 256;

    private float x, z;
    private ModelRaw model;
    private TextureTerrainPack texturePack;
    private TextureTerrain blendMap;
    private float[][] heights;
    private String heightMap;

    public Terrain(TextureTerrainPack texturePack, String heightMap) {
        this.texturePack = texturePack;
        this.blendMap = texturePack.getBlendMapTexture();
        this.heightMap = heightMap;
        this.model = generateTerrain(heightMap);
    }

    private ModelRaw generateTerrain(String heightMap) {

        BufferedImage image = null;

        try {
            image = ImageIO.read(new File("res/" + heightMap + ".png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        int vertexCnt = image != null ? image.getHeight() : 0;
        heights = new float[vertexCnt][vertexCnt];
        int count = vertexCnt * vertexCnt;
        float[] vertices = new float[count * 3];
        float[] normals = new float[count * 3];
        float[] textureCoords = new float[count * 2];
        int[] indices = new int[6 * (vertexCnt - 1) * (vertexCnt - 1)];
        int vertexPointer = 0;

        for(int i = 0; i < vertexCnt; i++){
            for(int j = 0; j < vertexCnt; j++){
                heights[j][i] = getHeight(j, i, image);
                vertices[vertexPointer * 3] = (float) j /((float) vertexCnt - 1) * SIZE;
                vertices[vertexPointer * 3 + 1] = getHeight(j, i, image);
                vertices[vertexPointer * 3 + 2] = (float) i /((float) vertexCnt - 1) * SIZE;
                Vector3f normal = calculateNormal(j, i, image);
                normals[vertexPointer * 3] = normal.x;
                normals[vertexPointer * 3 + 1] = normal.y;
                normals[vertexPointer * 3 + 2] = normal.z;
                textureCoords[vertexPointer * 2] = (float) j /((float) vertexCnt - 1);
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

        return EverPhaseApi.RESOURCE_LOADER.loadToVAO(new ModelData(vertices, textureCoords, normals, indices, 0));
    }

    private float getHeight(int x, int z, BufferedImage image) {
        if(x < 0 || x >= image.getHeight() || z < 0 || z >= image.getHeight()) return 0;

        float height = image.getRGB(x, z);
        height += MAX_PIXEL_COLOR / 2.0F;
        height /= MAX_PIXEL_COLOR / 2.0F;
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
    public float getHeightOfTerrain(float worldX, float worldZ) {
        float terrainX = worldX - this.x;
        float terrainZ = worldZ - this.z;
        float gridSquareSize = SIZE / ((float) heights.length -1);
        int gridX = (int) Math.floor(terrainX / gridSquareSize);
        int gridZ = (int) Math.floor(terrainZ / gridSquareSize);

        if(gridX >= heights.length - 1 || gridZ >= heights.length -1 || gridX < 0 || gridZ < 0) return 0;

        float xCoord = (terrainX % gridSquareSize) / gridSquareSize;
        float zCoord = (terrainZ % gridSquareSize) / gridSquareSize;

        if (xCoord <= (1-zCoord)) {
            return Maths
                    .barryCentric(new Vector3f(0, heights[gridX][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ], 0), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        } else {
            return Maths
                    .barryCentric(new Vector3f(1, heights[gridX + 1][gridZ], 0), new Vector3f(1,
                            heights[gridX + 1][gridZ + 1], 1), new Vector3f(0,
                            heights[gridX][gridZ + 1], 1), new Vector2f(xCoord, zCoord));
        }
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

    public void setGridPosition(int centerX, int centerZ) {
        this.x = centerX * SIZE;
        this.z = centerZ * SIZE;
    }
}
