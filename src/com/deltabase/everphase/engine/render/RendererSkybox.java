package com.deltabase.everphase.engine.render;

import com.deltabase.everphase.engine.model.ModelRaw;
import com.deltabase.everphase.engine.modelloader.ResourceLoader;
import com.deltabase.everphase.engine.shader.ShaderSkybox;
import com.deltabase.everphase.entity.EntityPlayer;
import com.deltabase.everphase.main.Main;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RendererSkybox {


    private static final float SKYBOX_SIZE = 500f;          //The size of one side of the sky box

    private static final float[] VERTICES = {               //The vertex positions of every vertex of the sky box
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

    private static String[] DAY_TEXTURE_FILES = { "day/right", "day/left", "day/top", "day/bottom", "day/back", "day/front"};               //The daytime sky box
    private static String[] NIGHT_TEXTURE_FILES = { "night/right", "night/left", "night/top", "night/bottom", "night/back", "night/front"}; //The nighttime sky box

    private ModelRaw cube;                                                  //The sky box model
    private int dayTexture, nightTexture;                                   //The texture locations of the day- and night sky texture
    private ShaderSkybox shader = new ShaderSkybox();                       //The sky box shader

    private float timeOfDay = 12000;                                         //The time of the day

    public RendererSkybox(ResourceLoader loader, Matrix4f projectionMatrix) {
        cube = loader.loadToVAO(VERTICES, 3);
        dayTexture = loader.loadCubeMap(DAY_TEXTURE_FILES);
        nightTexture = loader.loadCubeMap(NIGHT_TEXTURE_FILES);
        shader.start();
        shader.connectTextureUnits();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    /*
     *
     */
    public void render(EntityPlayer player, float fogR, float fogG, float fogB) {
        shader.start();
        shader.loadViewMatrix(player);
        shader.loadFogColor(fogR, fogG, fogB);
        GL30.glBindVertexArray(cube.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        bindTextures();
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, cube.getVertexCnt());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    private void bindTextures(){
        timeOfDay += 10 * Main.getFrameTimeSeconds();
        timeOfDay %= 24000;
        int texture1;
        int texture2;
        float blendFactor;
        if(timeOfDay >= 0 && timeOfDay < 5000){
            texture1 = nightTexture;
            texture2 = nightTexture;
            blendFactor = (timeOfDay - 0)/(5000);
        }else if(timeOfDay >= 5000 && timeOfDay < 8000){
            texture1 = nightTexture;
            texture2 = dayTexture;
            blendFactor = (timeOfDay - 5000)/(8000 - 5000);
        }else if(timeOfDay >= 8000 && timeOfDay < 21000){
            texture1 = dayTexture;
            texture2 = dayTexture;
            blendFactor = (timeOfDay - 8000)/(21000 - 8000);
        }else{
            texture1 = dayTexture;
            texture2 = nightTexture;
            blendFactor = (timeOfDay - 21000)/(24000 - 21000);
        }

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture1);
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, texture2);
        shader.loadBlendFactor(blendFactor);
    }

}