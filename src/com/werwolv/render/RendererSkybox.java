package com.werwolv.render;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.model.ModelRaw;
import com.werwolv.shader.ShaderSkybox;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RendererSkybox {

    private static final float SKYBOX_SIZE = 500f;

    private static final float[] VERTICES = {
            -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
            -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
            SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
            SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
            SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
            -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,

            -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
            -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
            -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
            -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
            -SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
            -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,

            SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
            SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
            SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
            SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
            SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
            SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,

            -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
            -SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
            SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
            SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
            SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
            -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,

            -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
            SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,
            SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
            SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
            -SKYBOX_SIZE,  SKYBOX_SIZE,  SKYBOX_SIZE,
            -SKYBOX_SIZE,  SKYBOX_SIZE, -SKYBOX_SIZE,

            -SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
            -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
            SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
            SKYBOX_SIZE, -SKYBOX_SIZE, -SKYBOX_SIZE,
            -SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE,
            SKYBOX_SIZE, -SKYBOX_SIZE,  SKYBOX_SIZE
    };

    private static String[] TEXTURE_FILES = { "day/right", "day/left", "day/top", "day/bottom", "day/back", "day/front"};

    private ModelRaw cube;
    private int texture;
    private ShaderSkybox shader = new ShaderSkybox();

    public RendererSkybox(ModelLoader loader, Matrix4f projectionMatrix) {
        cube = loader.loadToVAO(VERTICES, 3);
        texture = loader.loadCubeMap(TEXTURE_FILES);
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(EntityPlayer player) {
        shader.start();
        shader.loadViewMatrix(player);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCnt());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

}