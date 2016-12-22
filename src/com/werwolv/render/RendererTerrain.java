package com.werwolv.render;

import com.werwolv.model.ModelRaw;
import com.werwolv.resource.TextureModel;
import com.werwolv.shader.ShaderTerrain;
import com.werwolv.terrain.Terrain;
import com.werwolv.toolbox.Maths;
import com.werwolv.toolbox.ValueNoise;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class RendererTerrain {

    private ShaderTerrain shader;

    private ValueNoise map = new ValueNoise(800, 800);

    public RendererTerrain(ShaderTerrain shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(List<Terrain> terrains) {
        for(Terrain terrain : terrains) {
            prepareTerrain(terrain);
            loadModelMatrix(terrain);
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCnt(), GL11.GL_UNSIGNED_INT, 0);
            unbindModels();
        }
    }

    private void prepareTerrain(Terrain terrain) {
        ModelRaw rawModel = terrain.getModel();
        TextureModel texture = terrain.getTexture();

        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        shader.loadShineVars(texture.getShineDamper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
    }

    private void unbindModels() {
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 200 * map.getHeightmap()[(int)terrain.getX()][(int)terrain.getZ()], terrain.getZ()), 0.0F, 0.0F, 0.0F, 1);
        shader.loadTransformationMatrix(transformationMatrix);

    }
}
