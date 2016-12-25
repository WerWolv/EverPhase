package com.werwolv.fbo;

public class FrameBufferMinimap extends FrameBufferObject{

    public static final int FRAMEBUFFER_RESOLUTION = 720;

    private int fboMinimap, textureMinimap, reflectionDepthBuffer;

    public FrameBufferMinimap(){
        initMinimapFBO();
    }

    private void initMinimapFBO() {
        fboMinimap = createFrameBuffer();
        textureMinimap = createTextureAttachment(1280,FRAMEBUFFER_RESOLUTION);
        reflectionDepthBuffer = createDepthBufferAttachment(1280,FRAMEBUFFER_RESOLUTION);
        unbindCurrentFrameBuffer();
    }

    public void bindMinimapFrameBuffer() {
        bindFrameBuffer(fboMinimap, 1280, FRAMEBUFFER_RESOLUTION);
    }

    public int getMiniMapTexture() {
        return textureMinimap;
    }

}
