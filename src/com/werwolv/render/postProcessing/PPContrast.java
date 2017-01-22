package com.werwolv.render.postProcessing;

import com.werwolv.render.RendererImage;
import com.werwolv.shader.ShaderContrast;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class PPContrast {

    private RendererImage renderer;
    private ShaderContrast shader;

    public PPContrast() {
        this.renderer = new RendererImage();
        this.shader = new ShaderContrast();
    }

    public void render(int texture) {
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.stop();
    }

    public void clean() {
        renderer.cleanUp();
        shader.clean();
    }

}
