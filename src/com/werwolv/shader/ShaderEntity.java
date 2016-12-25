package com.werwolv.shader;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.EntityLight;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;

public class ShaderEntity extends Shader {

    private static final int MAX_LIGHTS = 16;

    private int loc_transMatrix, loc_projMatrix, loc_viewMatrix;
    private int loc_lightPos[], loc_lightColor[], loc_attenuation[];
    private int loc_shineDamper, loc_reflectivity;
    private int loc_useFakeLightning;
    private int loc_skyColor;
    private int loc_numOfRows, loc_offset;
    private int loc_plane;

    public ShaderEntity() {
        super("shaderEntity", "shaderEntity");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
    }

    @Override
    protected void getAllUniformLocations() {
        loc_transMatrix = super.getUniformLocation("transformationMatrix");
        loc_projMatrix = super.getUniformLocation("projectionMatrix");
        loc_viewMatrix = super.getUniformLocation("viewMatrix");

        loc_shineDamper = super.getUniformLocation("shineDamper");
        loc_reflectivity = super.getUniformLocation("reflectivity");
        loc_useFakeLightning = super.getUniformLocation("useFakeLightning");
        loc_skyColor = super.getUniformLocation("skyColor");

        loc_numOfRows = super.getUniformLocation("numOfRows");
        loc_offset = super.getUniformLocation("offset");

        loc_plane = super.getUniformLocation("plane");

        loc_lightPos = new int[MAX_LIGHTS];
        loc_lightColor = new int[MAX_LIGHTS];
        loc_attenuation = new int[MAX_LIGHTS];

        for(int i = 0; i < MAX_LIGHTS; i++) {
            loc_lightPos[i] = super.getUniformLocation("lightPos[" + i + "]");
            loc_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            loc_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void loadNumOfRows(int numOfRows) {
        super.loadFloat(loc_numOfRows, numOfRows);
    }

    public void loadOffset(float x, float y) {
        super.loadVector(loc_offset, new Vector2f(x, y));
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_transMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_projMatrix, matrix);
    }

    public void loadViewMatrix(EntityPlayer camera) {
        super.loadMatrix(loc_viewMatrix, Maths.createViewMatrix(camera));
    }

    public void loadLights(List<EntityLight> lights) {
        for(int i = 0; i < MAX_LIGHTS; i++) {
            if(i < lights.size()) {
                super.loadVector(loc_lightPos[i], lights.get(i).getPosition());
                super.loadVector(loc_lightColor[i], lights.get(i).getColor());
                super.loadVector(loc_attenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(loc_lightPos[i], new Vector3f(0.0F, 0.0F, 0.0F));
                super.loadVector(loc_lightColor[i], new Vector3f(0.0F, 0.0F, 0.0F));
                super.loadVector(loc_attenuation[i], new Vector3f(1.0F, 0.0F, 0.0F));}
        }
    }

    public void loadShineVars(float damper, float reflectivity) {
        super.loadFloat(loc_shineDamper, damper);
        super.loadFloat(loc_reflectivity, reflectivity);
    }

    public void loadFakeLightningVar(boolean useFakeLightning) {
        super.loadBoolean(loc_useFakeLightning, useFakeLightning);
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(loc_skyColor, new Vector3f(r, g, b));
    }

    public void loadClipPlane(Vector4f plane) {
        super.loadVector(loc_plane, plane);
    }
}
