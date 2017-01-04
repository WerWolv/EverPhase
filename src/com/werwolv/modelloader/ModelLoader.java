package com.werwolv.modelloader;

import com.werwolv.gui.GuiTextureUnit;
import com.werwolv.model.ModelRaw;
import com.werwolv.resource.Texture;
import com.werwolv.resource.TextureData;
import de.matthiasmann.twl.utils.PNGDecoder;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;


public class ModelLoader {

    private List<Integer> vaos = new ArrayList<>();         //All Vertex Array Objects for cleanup
    private List<Integer> vbos = new ArrayList<>();         //All Vertex Buffer Objects for cleanup
    private List<Integer> textures = new ArrayList<>();     //All Textures for cleanup
    private List<Integer> texturesSize = new ArrayList<>();

    /*
     * Create a new Model that can be rendered to the screen using the data
     * from the loaded obj file
     *
     * @param data  The model data returned by the OBJModelLoader class
     * @return      The model usable for rendering
     */
    public ModelRaw loadToVAO(ModelData data) {
        int vaoID = createVAO();                            //Create a new Vertex Array Object
        bindIndicesBuffer(data.getIndices());               //Bind the indices to the indices buffer
        storeDataInAttrList(0, 3, data.getVertices());  //Bind the vertices to the first VBO
        storeDataInAttrList(1, 2, data.getTextureCoords()); //Bind the texture coordinates to the second VBO
        storeDataInAttrList(2, 3, data.getNormals());   //Bind the normals to the third  VBO
        unbindVAO();                                                    //Unbind the VAO

        return new ModelRaw(vaoID, data.getIndices().length);           //Return a new Model with the id of the model and the amount of indices
    }

    /*
     * Create a new Model that can be rendered to the screen using the data
     * from the loaded obj file
     *
     * @param data  The model data returned by the OBJModelLoader class
     * @return      The model usable for rendering
     */
    public ModelRaw loadToVAO(ModelDataNM data) {
        int vaoID = createVAO();                            //Create a new Vertex Array Object
        bindIndicesBuffer(data.getIndices());               //Bind the indices to the indices buffer
        storeDataInAttrList(0, 3, data.getVertices());  //Bind the vertices to the first VBO
        storeDataInAttrList(1, 2, data.getTextureCoords()); //Bind the texture coordinates to the second VBO
        storeDataInAttrList(2, 3, data.getNormals());   //Bind the normals to the third  VBO
        storeDataInAttrList(3, 3, data.getTangents());
        unbindVAO();                                                    //Unbind the VAO

        return new ModelRaw(vaoID, data.getIndices().length);           //Return a new Model with the id of the model and the amount of indices
    }


    /*
     * Create a new Model that can be rendered to the screen using the
     * passed vertices. This is used for 2D Rendering (GUIs) and
     * Boxmodels (Skymaps).
     *
     * @param positions     The vertices of the model
     * @param dimensions    The dimensions of the object (2 for GUIs, 3 for Skymaps)
     * @return      The model usable for rendering
     */
    public ModelRaw loadToVAO(float[] positions, int dimensions) {
        int vaoID = createVAO();                                        //Create a new VAO
        this.storeDataInAttrList(0, dimensions, positions);      //Store the vertices in the first VBO
        unbindVAO();                                                    //Unbind the VAO

        return new ModelRaw(vaoID, positions.length / dimensions);
    }

    /*
     * Create a new VAO
     *
     * @return  The id of the VAO
     */
    private int createVAO() {
        int vaoID = GL30.glGenVertexArrays();                           //Generate a new VAO and store the id of it
        vaos.add(vaoID);                                                //Add the created VAO to the list of VAOs
        GL30.glBindVertexArray(vaoID);                                  //Bind the created VAO to memory

        return vaoID;
    }

    /*
     * Create a new Vertex Buffer Object and store data in it
     *
     * @param attrNr    The index in the VBO
     * @param coorSize  The amount of coordinates
     * @param data      The data that should get stored in the VBO
     */
    private void storeDataInAttrList(int attrNr, int coordSize, float[] data) {
        int vboID = GL15.glGenBuffers();                                            //Generate a new VBO

        vbos.add(vboID);                                                            //Add the created VBO to the list of VBOs
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID);                             //Bind the VBO to memory
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, storeDataInFloatBuffer(data), GL15.GL_STATIC_DRAW);     //Store the data in a float buffer to pass it to the VBO

        GL20.glVertexAttribPointer(attrNr, coordSize, GL11.GL_FLOAT, false, 0, 0);  //Set the pointer to the specified index
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0);                             //Unbind the VBO
    }

    /*
     * Create a new buffer for indices
     *
     * @param indices   An array of indices for a model
     */
    private void bindIndicesBuffer(int[] indices) {
        int vboID = GL15.glGenBuffers();                                                                        //Create a new VBO
        vbos.add(vboID);                                                                                        //Add the VBO to the list of VBOs

        GL15.glBindBuffer(GL15.GL_ELEMENT_ARRAY_BUFFER, vboID);                                                 //Bind the VBO to memory
        GL15.glBufferData(GL15.GL_ELEMENT_ARRAY_BUFFER, storeDataInIntBuffer(indices), GL15.GL_STATIC_DRAW);    //Store the indices in the VBO
    }

    /*
     * Delete all VAOs, VBOs and textures from memory
     */
    public void clean() {
        for(int vao : vaos)
            GL30.glDeleteVertexArrays(vao);

        for(int vbo : vbos)
            GL15.glDeleteBuffers(vbo);

        for(int texture : textures)
            GL11.glDeleteTextures(texture);
    }

    /*
     * Load a texture into memory and get its id
     *
     * @param fileName  The name of the png texture file located inside the /res folder
     * @return The texture id
     */
    public int loadTexture(String fileName) {
        Texture texture = Texture.loadTexture("res/" + fileName + ".png");                      //Load the texture

        textures.add(texture.getTextureId());                                                   //Add the texture to the list of textures for cleanup
        texturesSize.add(texture.getWidth());

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);   //Set the mipmap detail level to decrease linearly
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);       //Set the texture to repeat itself if it gets drawn on a bigger surface
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        return texture.getTextureId();
    }

    public GuiTextureUnit loadGuiTexture(String fileName) {
        Texture texture = Texture.loadTexture("res/" + fileName + ".png");                      //Load the texture

        textures.add(texture.getTextureId());                                                   //Add the texture to the list of textures for cleanup
        texturesSize.add(texture.getWidth());

        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);   //Set the mipmap detail level to decrease linearly
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);       //Set the texture to repeat itself if it gets drawn on a bigger surface
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);

        return new GuiTextureUnit(0, 0, 1, texture.getWidth(), texture.getTextureId());
    }

    /*
     * Create a cube map out of 6 different cube side textures
     *
     * @param textureFiles  The textures in the specific order: right, left, top, bottom, back, front
     * @return              The texture id of the cube map
     */
    public int loadCubeMap(String[] textureFiles) {
        int textureID = GL11.glGenTextures();                   //Create a new texture id
        GL13.glActiveTexture(GL13.GL_TEXTURE0);                 //Activate the first texture buffer
        GL11.glBindTexture(GL13.GL_TEXTURE_CUBE_MAP, textureID);//Bind the texture of the cubemap to the buffer

        for(int i = 0; i < textureFiles.length; i++) {          //For each texture...
            TextureData data = decodeTextureFile("res/skyboxes/" + textureFiles[i] + ".png");   //Load the textures and create a cube map out of them
            GL11.glTexImage2D(GL13.GL_TEXTURE_CUBE_MAP_POSITIVE_X + i, 0,       //Set the texture to the right side of the cube
                    GL11.GL_RGBA, data.getWidth(), data.getHeight(),
                    0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, data.getBuffer());
        }

        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR); //Set the mipmap detail level to decrease linearly
        GL11.glTexParameteri(GL13.GL_TEXTURE_CUBE_MAP, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);

        textures.add(textureID);                                                                      //Add the texture id to the list of textures for cleanup

        return textureID;
    }

    /*
     * Loads a png file into a TextureData Object
     *
     * @param fileName  The name of the file stored in /res
     * @return          The created TextureData Object
     */
    private TextureData decodeTextureFile(String fileName) {
        int width = 0;
        int height = 0;
        ByteBuffer buffer = null;
        try {
            FileInputStream in = new FileInputStream(fileName);
            PNGDecoder decoder = new PNGDecoder(in);
            width = decoder.getWidth();
            height = decoder.getHeight();
            buffer = ByteBuffer.allocateDirect(4 * width * height);
            decoder.decode(buffer, width * 4, PNGDecoder.Format.RGBA);
            buffer.flip();
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Tried to load texture " + fileName + ", didn't work");
        }
        return new TextureData(buffer, width, height);
    }

    private void unbindVAO() {
        GL30.glBindVertexArray(0);
    }

    /*
     * Convert a float array to a float buffer to pass to the VBO
     *
     * @param data  The float array to convert
     * @return      The converted float buffer
     */
    private FloatBuffer storeDataInFloatBuffer(float[] data) {
        return (FloatBuffer) BufferUtils.createFloatBuffer(data.length).put(data).flip();
    }

    /*
     * Convert a in array to a int buffer to pass to the VBO
     *
     * @param data  The int array to convert
     * @return      The converted int buffer
     */
    private IntBuffer storeDataInIntBuffer(int[] data) {
        return (IntBuffer) BufferUtils.createIntBuffer(data.length).put(data).flip();
    }}
