package com.werwolv.render;

import com.werwolv.gui.Gui;
import com.werwolv.model.ModelRaw;
import com.werwolv.shader.ShaderGui;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class RendererGui {

    private final ModelRaw quad;
    private ShaderGui shader;

    public RendererGui(ModelLoader loader) {
        float[] positions = { -1, 1, -1, -1, 1, 1, 1, -1};
        quad = loader.loadToVAO(positions);

        shader = new ShaderGui();
    }

    public void render(List<Gui> guis) {
        shader.start();
        GL30.glBindVertexArray(quad.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glDisable(GL11.GL_DEPTH_TEST);


        for(Gui gui : guis) {
            GL13.glActiveTexture(GL13.GL_TEXTURE0);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, gui.getTexture());

            Matrix4f matrix = Maths.createTransformationMatrix(gui.getPosition(), gui.getScale());
            shader.loadTransformationMatrix(matrix);

            GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCnt());
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
        shader.stop();
    }

    public void clean() {
        shader.clean();
    }
}
