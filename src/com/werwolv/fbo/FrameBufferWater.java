package com.werwolv.fbo;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

public class FrameBufferWater extends FrameBufferObject{

    private static final int REFLECTION_WIDTH = 320;
    private static final int REFLECTION_HEIGHT = 180;

    private static final int REFRACTION_WIDTH = 1280;
    private static final int REFRACTION_HEIGHT = 720;

    private int reflectionFrameBuffer;
    private int reflectionTexture;
    private int reflectionDepthBuffer;

    private int refractionFrameBuffer;
    private int refractionTexture;
    private int refractionDepthTexture;

    public FrameBufferWater() {
        bindFrameBuffer(reflectionFrameBuffer,REFLECTION_WIDTH,REFLECTION_HEIGHT);      //Bind the frame buffer used for reflection of the water to memory
        bindFrameBuffer(refractionFrameBuffer,REFRACTION_WIDTH,REFRACTION_HEIGHT);      //Bind the frame buffer used for the transparent part of the water to memory
    }

    public void clean() {
        GL30.glDeleteFramebuffers(reflectionFrameBuffer);   //Delete the frame buffer from memory
        GL11.glDeleteTextures(reflectionTexture);           //Delete the texture from memory
        GL30.glDeleteRenderbuffers(reflectionDepthBuffer);  //Delete the depth buffer from memory
        GL30.glDeleteFramebuffers(refractionFrameBuffer);   //Delete the frame buffer from memory
        GL11.glDeleteTextures(refractionTexture);           //Delete the texture from memory
        GL11.glDeleteTextures(refractionDepthTexture);      //Delete the depth buffer from memory
    }

    private void initReflectionFrameBuffer() {
        reflectionFrameBuffer = createFrameBuffer();                                             //Create a new frame buffer object
        reflectionTexture = createTextureAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT);         //Create a new texture attachment for the reflection FBO
        reflectionDepthBuffer = createDepthBufferAttachment(REFLECTION_WIDTH,REFLECTION_HEIGHT); //Create a new depth buffer attachment for the reflection FBO
        unbindCurrentFrameBuffer();                                                              //Unbind the reflection FBO
    }

    private void initRefractionFrameBuffer() {
        refractionFrameBuffer = createFrameBuffer();                                                //Create a new frame buffer object
        refractionTexture = createTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);            //Create a new texture attachment for the refraction FBO
        refractionDepthTexture = createDepthTextureAttachment(REFRACTION_WIDTH,REFRACTION_HEIGHT);  //Create a new depth buffer attachment for the refraction FBO
        unbindCurrentFrameBuffer();                                                                 //Unbind the refraction FBO
    }

    /* Getters */

    public int getReflectionTexture() {
        return reflectionTexture;
    }

    public int getRefractionTexture() {
        return refractionTexture;
    }

    public int getRefractionDepthTexture(){
        return refractionDepthTexture;
    }

}