package com.werwolv.fbo;

public class FrameBufferMinimap extends FrameBufferObject{

    private static final int FRAMEBUFFER_WIDTH = 1280;           //The width of the Minimap
    private static final int FRAMEBUFFER_HEIGHT = 720;           //The height of the Minimap

    private int fboMinimap, textureMinimap, minimapDepthBuffer;  //Addresses of the fbo, the texture and the depth buffer

    public FrameBufferMinimap(){
        initMinimapFBO();
    }

    private void initMinimapFBO() {
        fboMinimap = createFrameBuffer();   //Create a new framebuffer in memory and save the address
        textureMinimap = createTextureAttachment(FRAMEBUFFER_WIDTH,FRAMEBUFFER_HEIGHT); //Create a new texture and bind the framebuffer to it
        minimapDepthBuffer = createDepthBufferAttachment(FRAMEBUFFER_WIDTH,FRAMEBUFFER_HEIGHT); //Create a new depthbuffer and save the address
        unbindCurrentFrameBuffer();     //Unbind the current framebuffer to render to the main screen again
    }

    /*
     * Bind the framebuffer to draw to it instead of the main screen
     */
    public void bindMinimapFrameBuffer() {
        bindFrameBuffer(fboMinimap, FRAMEBUFFER_WIDTH, FRAMEBUFFER_HEIGHT);
    }

    public int getMiniMapTexture() {
        return textureMinimap;
    }

}
