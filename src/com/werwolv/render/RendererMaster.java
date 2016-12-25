package com.werwolv.render;

import com.werwolv.entity.Entity;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.EntityLight;
import com.werwolv.main.Main;
import com.werwolv.model.ModelTextured;
import com.werwolv.shader.ShaderEntity;
import com.werwolv.shader.ShaderTerrain;
import com.werwolv.terrain.Terrain;
import com.werwolv.terrain.TileWater;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
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

    private ShaderEntity shader = new ShaderEntity();
    private ShaderTerrain terrainShader = new ShaderTerrain();

    private RendererEntity  rendererEntity;
    private RendererTerrain rendererTerrain;
    private RendererSkybox  rendererSkybox;
    private RendererWater   rendererWater;

    private Map<ModelTextured, List<Entity>> entities = new HashMap<>();
    private List<Terrain> terrains = new ArrayList<>();

    public RendererMaster(ModelLoader loader) {
        enableCulling();

        createProjectionMatrix();
        rendererEntity = new RendererEntity(shader, projectionMatrix);
        rendererTerrain = new RendererTerrain(terrainShader, projectionMatrix);
        rendererSkybox = new RendererSkybox(loader, projectionMatrix);
        rendererWater = new RendererWater(loader, projectionMatrix);
    }

    public void renderScene(List<Entity> entities, List<Terrain> terrains, List<TileWater> waters, List<EntityLight> lights, EntityPlayer player, Vector4f clipPlane) {
        for(Terrain terrain : terrains) processTerrains(terrain);
        for(Entity entity : entities) processEntity(entity);

        this.render(lights, player, clipPlane);
        rendererWater.render(waters, player);
    }

    public void render(List<EntityLight> lights, EntityPlayer player, Vector4f clipPlane) {
        init();
        shader.start();
        shader.loadClipPlane(clipPlane);
        shader.loadSkyColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
        shader.loadLights(lights);
        shader.loadViewMatrix(player);

        rendererEntity.render(entities);

        shader.stop();

        terrainShader.start();
        terrainShader.loadClipPlane(clipPlane);
        terrainShader.loadSkyColor(SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
        terrainShader.loadLights(lights);
        terrainShader.loadViewMatrix(player);
        rendererTerrain.render(terrains);
        terrainShader.stop();

        rendererSkybox.render(player, SKY_COLOR.x, SKY_COLOR.y, SKY_COLOR.z);
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
        rendererWater.clean();
    }
}
