package com.deltabase.everphase.engine.render;

import com.deltabase.everphase.engine.model.ModelRaw;
import com.deltabase.everphase.engine.model.ModelTextured;
import com.deltabase.everphase.engine.resource.TextureModel;
import com.deltabase.everphase.engine.shader.ShaderNormalMapping;
import com.deltabase.everphase.engine.toolbox.Maths;
import com.deltabase.everphase.entity.Entity;
import com.deltabase.everphase.entity.EntityLight;
import com.deltabase.everphase.entity.EntityPlayer;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class RendererNormalMapping {

    private ShaderNormalMapping shader;

    public RendererNormalMapping(Matrix4f projectionMatrix) {
        this.shader = new ShaderNormalMapping();
        shader.start();
        shader.loadProjectionMatrix(projectionMatrix);
        shader.connectTextureUnits();
        shader.stop();
    }

    public void render(Map<ModelTextured, List<Entity>> entities, Vector4f clipPlane, List<EntityLight> lights, EntityPlayer player) {
        shader.start();
        prepare(clipPlane, lights, player);
        for (ModelTextured model : entities.keySet()) {
            prepareTexturedModel(model);
            List<Entity> batch = entities.get(model);
            for (Entity entity : batch) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModelRaw().getVertexCnt(), GL11.GL_UNSIGNED_INT, 0);
            }
            unbindTexturedModel();
        }
        shader.stop();
    }

    public void clean() {
        shader.clean();
    }

    private void prepareTexturedModel(ModelTextured model) {
        ModelRaw rawModel = model.getModelRaw();
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
        GL20.glEnableVertexAttribArray(1);
        GL20.glEnableVertexAttribArray(2);
        GL20.glEnableVertexAttribArray(3);
        TextureModel texture = model.getTexture();
        shader.loadNumberOfRows(texture.getNumOfRows());
        if (texture.hasTransparency()) {
            RendererMaster.disableCulling();
        }
        shader.loadShineVariables(texture.getShineDamper(), texture.getReflectivity());
        GL13.glActiveTexture(GL13.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());
        GL13.glActiveTexture(GL13.GL_TEXTURE1);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getNormalMapID());

        shader.loadUseExtraInfoMap(texture.hasExtraInfoMap());

        if(texture.hasExtraInfoMap()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE2);         //Activate the second texture store
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getExtraInfoMapID());  //Bind the texture of the model to memory
        }
    }

    private void unbindTexturedModel() {
        RendererMaster.enableCulling();
        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);
        GL20.glDisableVertexAttribArray(3);
        GL30.glBindVertexArray(0);
    }

    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(),
                entity.getRotY(), entity.getRotZ(), entity.getScale());
        shader.loadTransformationMatrix(transformationMatrix);
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());
    }

    private void prepare(Vector4f clipPlane, List<EntityLight> lights, EntityPlayer player) {
        shader.loadClipPlane(clipPlane);
        //need to be public variables in MasterRenderer
        shader.loadSkyColor(RendererMaster.SKY_COLOR);
        Matrix4f viewMatrix = Maths.createViewMatrix(player);

        shader.loadLights(lights, viewMatrix);
        shader.loadViewMatrix(viewMatrix);
    }

}
