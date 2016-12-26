package com.werwolv.fbo;

import com.werwolv.main.Main;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;
import org.lwjgl.opengl.GL32;

import java.nio.ByteBuffer;

public class FrameBufferObject {

    /*
     * Unbinds the currently bound frame buffer
     */
    public void unbindCurrentFrameBuffer() {
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, 0);             //Unbind the current frame buffer
        GL11.glViewport(0, 0, Main.getWindowSize()[0], Main.getWindowSize()[1]);    //Reset the viewport to display the entire screen again
    }

    /*
     * Bind the frame buffer to the specified frame buffer address
     *
     * @param frameBuffer   The address of the frame buffer where the frame buffer is stored
     * @param width         The width of the frame buffer
     * @param height        The height of the frame buffer
     */
    protected void bindFrameBuffer(int frameBuffer, int width, int height){
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);          //Unbind the currently bound texture
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);   //Bind the new frame buffer
        GL11.glViewport(0, 0, width, height);                 //Set the viewport of the frame buffer to the specified width and height
    }

    /*
     * Create a new frame buffer and get the address of it
     *
     * @return The address of the created frame buffer
     */
    protected int createFrameBuffer() {
        int frameBuffer = GL30.glGenFramebuffers();                 //Generate a new frame buffer
        GL30.glBindFramebuffer(GL30.GL_FRAMEBUFFER, frameBuffer);   //Bind the created frame buffer
        GL11.glDrawBuffer(GL30.GL_COLOR_ATTACHMENT0);               //Set the buffer of the frame buffer to the first color attachment

        return frameBuffer;
    }

    /*
     * Create a new texture attachment
     *
     * @param width     The width of the texture
     * @param height    The height of the texture
     *
     * @return  The address of the created texture attachment
     */
    protected int createTextureAttachment(int width, int height) {
        int texture = GL11.glGenTextures();             //Generate a new texture and store it in memory
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);    //Bind the texture to memory
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGB, width, height,0, GL11.GL_RGB, GL11.GL_UNSIGNED_BYTE, (ByteBuffer) null); //Set the texture to display the rgb channel
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);   //Specify the mipmap level
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_COLOR_ATTACHMENT0, texture, 0);  //Add the currently bound texture attachment to the frame buffer
        return texture;
    }

    /*
     * Create a new depth texture attachment
     *
     * @param width     The width of the depth texture
     * @param height    The height of the depth texture
     *
     * @return  The address of the created depth texture attachment
     */
    protected int createDepthTextureAttachment(int width, int height){
        int texture = GL11.glGenTextures();             //Generate a new depth texture
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);    //Bind the generated attachment
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL14.GL_DEPTH_COMPONENT32, width, height,0, GL11.GL_DEPTH_COMPONENT, GL11.GL_FLOAT, (ByteBuffer) null);    //Set the texture to display the depth channel
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);   //Specify the mipmap level
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL32.glFramebufferTexture(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, texture, 0);   //Add the currently bound depth texture attachment to the framebuffer
        return texture;
    }

    /*
     * Create a new depth buffer attachment
     *
     * @param width     The width of the depth buffer
     * @param height    The height of the depth buffer
     *
     * @return  The address of the created depth buffer attachment
     */
    protected int createDepthBufferAttachment(int width, int height) {
        int depthBuffer = GL30.glGenRenderbuffers();            //Create a new render buffer
        GL30.glBindRenderbuffer(GL30.GL_RENDERBUFFER, depthBuffer);     //Bind the render buffer to memory
        GL30.glRenderbufferStorage(GL30.GL_RENDERBUFFER, GL11.GL_DEPTH_COMPONENT, width, height);   //Add the depth map to the render buffer
        GL30.glFramebufferRenderbuffer(GL30.GL_FRAMEBUFFER, GL30.GL_DEPTH_ATTACHMENT, GL30.GL_RENDERBUFFER, depthBuffer);   //Bind the depth buffer to the frame buffer
        return depthBuffer;
    }


}
