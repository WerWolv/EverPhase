package com.deltabase.everphase.render;

import com.deltabase.everphase.model.ModelRaw;
import com.deltabase.everphase.render.shadow.RendererShadowMapMaster;
import com.deltabase.everphase.resource.TextureTerrainPack;
import com.deltabase.everphase.shader.ShaderTerrain;
import com.deltabase.everphase.terrain.Terrain;
import com.deltabase.everphase.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class RendererTerrain {

    private ShaderTerrain shader;


    public RendererTerrain(ShaderTerrain shader, Matrix4f projectionMatrix) {
        this.shader = shader;

        shader.start();                                     //Start the terrain shader
        shader.connectTextureUnits();                       //Connect the 4 textures together in the way specified in the blend map
        shader.loadProjectionMatrix(projectionMatrix);      //Load up the projection matrix to the shader
        shader.loadMapSize(RendererShadowMapMaster.SHADOW_MAP_SIZE);
        shader.stop();                                      //Stop the terrain shader
    }

    /*
     * Render the terrain
     *
     * @param terrains  A list of all terrains to render
     */
    public void render(List<Terrain> terrains, Matrix4f toShadowSpace) {

        for(Terrain terrain : terrains) {       //For each terrain...
            prepareTerrain(terrain);            //...prepare the terrain for rendering...
            loadModelMatrix(terrain);           //...upload the transformation matrix to the shader...
            GL11.glDrawElements(GL11.GL_TRIANGLES, terrain.getModel().getVertexCnt(), GL11.GL_UNSIGNED_INT, 0); //...draw all triangles of the terrain...
            unbindModels();                     //...and unbind the model again
            shader.loadToShadowSpaceMatrix(toShadowSpace);
        }
    }

    /*
     * Prepare the terrain model for rendering
     *
     * @param terrain   The terrain to get rendered soon
     */
    private void prepareTerrain(Terrain terrain) {
        ModelRaw rawModel = terrain.getModel();         //Get the model of the passed terrain
        GL30.glBindVertexArray(rawModel.getVaoID());    //Load the model's VAO into memory
        GL20.glEnableVertexAttribArray(0);        //Unbind the VAO attribute for the model's vertices
        GL20.glEnableVertexAttribArray(1);        //Unbind the VAO attribute for the model's texture coords
        GL20.glEnableVertexAttribArray(2);        //Unbind the VAO attribute for the model's normals
        bindTextures(terrain);                          //Bind the terrain textures to the terrain model
        shader.loadShineVars(1, 0);   //Load the variables for shininess to the shader
    }

    /*
     * Bind the textures of the terrain to the terrain model
     *
     * @param terrain   The terrain to which the texture should get bound
     */
    private void bindTextures(Terrain terrain) {
        TextureTerrainPack terrainPack = terrain.getTexturePack();                                  //Get the 4 textures and the blend map from the terrain data

        GL13.glActiveTexture(GL13.GL_TEXTURE0);                                                     //Activate the first texture memory location
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainPack.getBackgroundTexture().getTextureID());  //Add the background texture to this location
        GL13.glActiveTexture(GL13.GL_TEXTURE1);                                                     //Activate the second texture memory location
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainPack.getrTexture().getTextureID());           //Add the first texture to this location
        GL13.glActiveTexture(GL13.GL_TEXTURE2);                                                     //Activate the third texture memory location
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainPack.getgTexture().getTextureID());           //Add the second texture to this location
        GL13.glActiveTexture(GL13.GL_TEXTURE3);                                                     //Activate the fourth texture memory location
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrainPack.getbTexture().getTextureID());           //Add the third texture to this location
        GL13.glActiveTexture(GL13.GL_TEXTURE4);                                                     //Activate the fifth texture memory location
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, terrain.getBlendMap().getTextureID());               //Add the blend map to this location
    }

    private void unbindModels() {
        GL20.glDisableVertexAttribArray(0);         //Unbind the VAO attribute for the model's vertices
        GL20.glDisableVertexAttribArray(1);         //Unbind the VAO attribute for the model's texture coords
        GL20.glDisableVertexAttribArray(2);         //Unbind the VAO attribute for the model's normals

        GL30.glBindVertexArray(0);                  //Unbind the model's VAO
    }

    /*
     * Creates a new transformation matrix and uploads it to the terrain shader
     */
    private void loadModelMatrix(Terrain terrain) {
        Matrix4f transformationMatrix = Maths.createTransformationMatrix(new Vector3f(terrain.getX(), 0.0F, terrain.getZ()), 0.0F, 0.0F, 0.0F, 1);

        shader.loadTransformationMatrix(transformationMatrix);

    }
}
