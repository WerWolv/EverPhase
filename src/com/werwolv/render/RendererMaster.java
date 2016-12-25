package com.werwolv.render;

import com.werwolv.entity.Entity;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.EntityLight;
import com.werwolv.main.Main;
import com.werwolv.model.ModelTextured;
import com.werwolv.shader.ShaderStatic;
import com.werwolv.shader.ShaderTerrain;
import com.werwolv.terrain.Terrain;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL11.glClearColor;

public class RendererMaster {

    private static final float FOV = 70;
    private static final float NEAR_PLANE = 0.1F;
    private static final float FAR_PLANE = 1000.0F;

    private static final Vector3f SKY_COLOR = new Vector3f(0.5F, 0.5F, 0.5F);

    private Matrix4f projectionMatrix;

    private ShaderStatic shader = new ShaderStatic();
    private ShaderTerrain terrainShader = new ShaderTerrain();

    private RendererEntity rendererEntity;
    private RendererTerrain rendererTerrain;

    private Map<ModelTextured, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public RendererMaster() {
        enableCulling();

        createProjectionMatrix();
        rendererEntity = new RendererEntity(shader, projectionMatrix);
        rendererTerrain = new RendererTerrain(terrainShader, projectionMatrix);
    }

    public void render(List<EntityLight> lights, EntityPlayer camera) {
        init();
        shader.start();
        shader.loadSkyColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
        shader.loadLights(lights);
        shader.loadViewMatrix(camera);

        rendererEntity.render(entities);

        shader.stop();

        terrainShader.start();
        terrainShader.loadSkyColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(camera);
        rendererTerrain.render(terrains);
        terrainShader.stop();

        entities.clear();
        terrains.clear();
    }

    private void init() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        glClearColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z, 1.0F);

    }


    private void createProjectionMatrix() {
        float aspectRatio = (float) Main.getWindowSize()[0] / (float) Main.getWindowSize()[1];
        float yScale = (float) ((1.0F / Math.tan(Math.toRadians(FOV / 2.0F))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustumLength));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustumLength));
        projectionMatrix.m33(0);
    }

    public void processTerrains(Terrain terrain) {
        terrains.add(terrain);
    }

    public void processEntity(Entity entity) {
        ModelTextured entityModel = entity.getModel();
        List<Entity> batch = entities.get(entityModel);

        if(batch != null)
            batch.add(entity);
        else {
            List<Entity> newBatch = new ArrayList<>();
            newBatch.add(entity);
            entities.put(entityModel, newBatch);
        }
    }

    public static void enableCulling() {
        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glCullFace(GL11.GL_BACK);
    }

    public static void disableCulling() {
        GL11.glDisable(GL11.GL_CULL_FACE);
    }

    public void clean() {
        shader.clean();
        terrainShader.clean();
    }
}
