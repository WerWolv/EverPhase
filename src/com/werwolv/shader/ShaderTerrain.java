package com.werwolv.shader;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.EntityLight;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ShaderTerrain extends Shader {

    private int loc_transMatrix, loc_projMatrix, loc_viewMatrix;
    private int loc_lightPos, loc_lightColor;
    private int loc_shineDamper, loc_reflectivity;
    private int loc_skyColor;
    private int loc_backgroundTexture, loc_rTexture, loc_gTexture, loc_bTexture, loc_blendMap;

    public ShaderTerrain() {
        super("shaderTerrain", "shaderTerrain");
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

        loc_lightPos = super.getUniformLocation("lightPos");
        loc_lightColor = super.getUniformLocation("lightColor");

        loc_shineDamper = super.getUniformLocation("shineDamper");
        loc_reflectivity = super.getUniformLocation("reflectivity");

        loc_skyColor = super.getUniformLocation("skyColor");

        loc_backgroundTexture = super.getUniformLocation("backgroundTexture");
        loc_rTexture = super.getUniformLocation("rTexture");
        loc_bTexture = super.getUniformLocation("gTexture");
        loc_gTexture = super.getUniformLocation("bTexture");
        loc_blendMap = super.getUniformLocation("blendMap");
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

    public void loadLight(EntityLight light) {
        super.loadVector(loc_lightPos, light.getPosition());
        super.loadVector(loc_lightColor, light.getColor());
    }

    public void loadShineVars(float damper, float reflectivity) {
        super.loadFloat(loc_shineDamper, damper);
        super.loadFloat(loc_reflectivity, reflectivity);
    }

    public void loadSkyColor(float r, float g, float b) {
        super.loadVector(loc_skyColor, new Vector3f(r, g, b));
    }

    public void connectTextureUnits() {
        super.loadInteger(loc_backgroundTexture, 0);
        super.loadInteger(loc_rTexture, 1);
        super.loadInteger(loc_gTexture, 2);
        super.loadInteger(loc_bTexture, 3);
        super.loadInteger(loc_blendMap, 4);

    }
}
