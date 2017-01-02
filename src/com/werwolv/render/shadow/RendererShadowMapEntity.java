package com.werwolv.render.shadow;

import com.werwolv.entity.Entity;
import com.werwolv.model.ModelRaw;
import com.werwolv.model.ModelTextured;
import com.werwolv.shader.ShaderShadow;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;


public class RendererShadowMapEntity {

    private Matrix4f projectionViewMatrix;
    private ShaderShadow shader;

    /**
     * @param shader               - the simple shader program being used for the shadow render
     *                             pass.
     * @param projectionViewMatrix - the orthographic projection matrix multiplied by the light's
     *                             "view" matrix.
     */
    public RendererShadowMapEntity(ShaderShadow shader, Matrix4f projectionViewMatrix) {
        this.shader = shader;
        this.projectionViewMatrix = projectionViewMatrix;
    }

    /**
     * Renders entieis to the shadow map. Each model is first bound and then all
     * of the entities using that model are rendered to the shadow map.
     *
     * @param entities - the entities to be rendered to the shadow map.
     */
    protected void render(Map<ModelTextured, List<Entity>> entities) {
        for (ModelTextured model : entities.keySet()) {
            ModelRaw rawModel = model.getModelRaw();
            bindModel(rawModel);
            for (Entity entity : entities.get(model)) {
                prepareInstance(entity);
                GL11.glDrawElements(GL11.GL_TRIANGLES, rawModel.getVertexCnt(),
                        GL11.GL_UNSIGNED_INT, 0);
            }
        }
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }

    /**
     * Binds a raw model before rendering. Only the attribute 0 is enabled here
     * because that is where the positions are stored in the VAO, and only the
     * positions are required in the vertex shader.
     *
     * @param rawModel - the model to be bound.
     */
    private void bindModel(ModelRaw rawModel) {
        GL30.glBindVertexArray(rawModel.getVaoID());
        GL20.glEnableVertexAttribArray(0);
    }

    /**
     * Prepares an entity to be rendered. The model matrix is created in the
     * usual way and then multiplied with the projection and view matrix (often
     * in the past we've done this in the vertex shader) to create the
     * mvp-matrix. This is then loaded to the vertex shader as a uniform.
     *
     * @param entity - the entity to be prepared for rendering.
     */
    private void prepareInstance(Entity entity) {
        Matrix4f modelMatrix = Maths.createTransformationMatrix(entity.getPosition(),
                entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());
        Matrix4f mvpMatrix = modelMatrix.mul(projectionViewMatrix);
        shader.loadMvpMatrix(mvpMatrix);
    }
}