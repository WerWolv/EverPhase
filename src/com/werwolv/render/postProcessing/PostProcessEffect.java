package com.werwolv.render.postProcessing;

import com.werwolv.render.RendererImage;
import com.werwolv.shader.Shader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class PostProcessEffect<T extends Shader> {

    private RendererImage renderer;
    private T shader;

    public PostProcessEffect(T shader, int width, int height) {
        renderer = new RendererImage(width, height);
        this.shader = shader;
    }

    public PostProcessEffect(T shader) {
        renderer = new RendererImage();
        this.shader = shader;
    }

    public void render(int texture) {
        shader.start();
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
        renderer.renderQuad();
        shader.stop();
    }

    public int getOutputTexture(){
        return renderer.getOutputTexture();
    }

    public void clean() {
        renderer.clean();
        shader.clean();
    }

    public RendererImage getRenderer() {
        return renderer;
    }

    public T getShader() {
        return shader;
    }
}
