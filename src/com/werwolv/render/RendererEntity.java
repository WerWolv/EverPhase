package com.werwolv.render;

import com.werwolv.entity.Entity;
import com.werwolv.main.Main;
import com.werwolv.model.ModelRaw;
import com.werwolv.model.ModelTextured;
import com.werwolv.resource.TextureModel;
import com.werwolv.shader.ShaderStatic;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class RendererEntity {

    private ShaderStatic shader;

    public RendererEntity(ShaderStatic shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.stop();
    }

    public void render(Map<ModelTextured, List<Entity>> entities) {
        for(ModelTextured model : entities.keySet()) {
            prepareModels(model);
            List<Entity> batch = entities.get(model);

            for(Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModelRaw().getVertexCnt(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindModels();
        }
    }

    private void prepareModels(ModelTextured model) {
        ModelRaw rawModel = model.getModelRaw();
        TextureModel texture = model.getTexture();

        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);

        if(texture.hasTransparency())
            RendererMaster.disableCulling();

        shader.loadFakeLightningVar(texture.useFakeLightning());
        shader.loadShineVars(texture.getShineDamper(), texture.getReflectivity());

        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
    }

    private void unbindModels() {
        RendererMaster.enableCulling();

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);

    }

}
