package com.werwolv.render;

import com.werwolv.entity.Entity;
import com.werwolv.main.Main;
import com.werwolv.model.ModelRaw;
import com.werwolv.model.ModelTextured;
import com.werwolv.shader.ShaderStatic;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class Renderer {

    public static final float FOV = 70;
    public static final float NEAR_PLANE = 0.1F;
    public static final float FAR_PLANE = 1000.0F;

    private Matrix4f projectionMatrix;

    public Renderer(ShaderStatic shader) {
        createProjectionMatrix();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void renderModel(Entity entity, ShaderStatic shader) {
        ModelTextured model = entity.getModel();
        ModelRaw rawModel = model.getModelRaw();

        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);

        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
        GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCnt(), GL11.GL_UNSIGNED_INT, 0);
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);

        GL30.glBindVertexArray(0);
    }

    private void createProjectionMatrix() {
        float aspectRatio = (float) Main.getWindowSize()[0] / (float) Main.getWindowSize()[1];
        float yScale = (float) ((1.0F / Math.tan(Math.toRadians(FOV / 2.0F))) * aspectRatio);
        float xScale = yScale / aspectRatio;
        float frustumLength = FAR_PLANE - NEAR_PLANE;

        projectionMatrix = new Matrix4f();
        projectionMatrix.m00(xScale);
        projectionMatrix.m11(yScale);
        projectionMatrix.m22(-((FAR_PLANE + NEAR_PLANE) / frustumLength));
        projectionMatrix.m23(-1);
        projectionMatrix.m32(-((2 * NEAR_PLANE * FAR_PLANE) / frustumLength));
        projectionMatrix.m33(0);
    }
}
