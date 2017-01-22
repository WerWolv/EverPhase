package com.werwolv.game.render;

import com.werwolv.game.entity.Entity;
import com.werwolv.game.model.ModelRaw;
import com.werwolv.game.resource.TextureModel;
import com.werwolv.game.shader.ShaderEntity;
import com.werwolv.game.toolbox.Maths;
import com.werwolv.game.model.ModelTextured;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;
import java.util.Map;

public class RendererEntity {

    private ShaderEntity shader;

    public RendererEntity(ShaderEntity shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.start();                                     //Start the shader rendering
        shader.loadProjectionMatrix(projectionMatrix);      //Load the projection matrix to the shader to add perspective
        shader.connectTextureUnits();
        shader.stop();                                      //Stop the shader rendering
    }

    /*
     * Render all entities
     *
     * @param entities  A Map with a List of all entities that use the same model so it only has to get bound once
     */
    public void render(Map<ModelTextured, List<Entity>> entities) {
        for(ModelTextured model : entities.keySet()) {      //For each textured model...
            prepareModels(model);                           //...prepare the models
            List<Entity> batch = entities.get(model);       //...add all existing entities to the list

            for(Entity entity : batch) {                    //For each entity currently in this list...
                prepareInstance(entity);                    //...create a new instance of the entity
                GL11.glDrawElements(GL11.GL_TRIANGLES, model.getModelRaw().getVertexCnt(), GL11.GL_UNSIGNED_INT, 0); //...draw the model to the screen
            }
            unbindModels();                                 //Unbind all models
        }
    }

    /*
     * Prepare the model for further rendering
     *
     * @param model     The model that should get rendererd
     */
    private void prepareModels(ModelTextured model) {
        ModelRaw rawModel = model.getModelRaw();        //Get the model data
        TextureModel texture = model.getTexture();      //Get the texture data

        GL30.glBindVertexArray(rawModel.getVaoID());    //Bind the vertices of the model to memory
        GL20.glEnableVertexAttribArray(0);       //Enable The index of the VBO in which the vertices VAO got stored
        GL20.glEnableVertexAttribArray(1);       //Enable The index of the VBO in which the texture coordinates VAO got stored
        GL20.glEnableVertexAttribArray(2);       //Enable The index of the VBO in which the normals VAO got stored

        shader.loadNumOfRows(texture.getNumOfRows());   //Set the number of rows of the texture atlas

        if(texture.hasTransparency())                   //If the loaded texture has transparency...
            RendererMaster.disableCulling();            //...disable the texture culling

        shader.loadFakeLightningVar(texture.useFakeLightning());                    //Enable fake lightning, if the normal lighting would look weird
        shader.loadShineVars(texture.getShineDamper(), texture.getReflectivity());  //Load the reflectivity and the dampness of the object to the shader for spectacular lightning

        GL13.glActiveTexture(GL13.GL_TEXTURE0);         //Activate the first texture store
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getTextureID());  //Bind the texture of the model to memory

        shader.loadUseExtraInfoMap(texture.hasExtraInfoMap());

        if(texture.hasExtraInfoMap()) {
            GL13.glActiveTexture(GL13.GL_TEXTURE1);         //Activate the first texture store
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, model.getTexture().getExtraInfoMapID());  //Bind the texture of the model to memory
        }
    }

    /*
     * Unbind all models
     */
    private void unbindModels() {
        RendererMaster.enableCulling();

        GL20.glDisableVertexAttribArray(0);
        GL20.glDisableVertexAttribArray(1);
        GL20.glDisableVertexAttribArray(2);

        GL30.glBindVertexArray(0);
    }

    /*
     * Prepare a new instance of an entity that should get rendered
     *
     * @param entity    The entity that should get rendered
     */
    private void prepareInstance(Entity entity) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(entity.getPosition(), entity.getRotX(), entity.getRotY(), entity.getRotZ(), entity.getScale());    //Create a fresh transformation matrix
        shader.loadTransformationMatrix(transformationMatrix);  //Load the transformation matrix to the shader
        shader.loadOffset(entity.getTextureXOffset(), entity.getTextureYOffset());  //Load the x and y offset of the texture on the texture atlas to the shader

    }

}
