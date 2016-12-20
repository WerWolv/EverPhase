package com.werwolv.render;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

    public void renderModel(RawModel model) {
        GL30.glBindVertexArray(model.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL11.glDrawElements(GL11.GL_TRIANGLES, model.getVertexCnt(), GL11.GL_UNSIGNED_INT, 0);
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCnt());
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
