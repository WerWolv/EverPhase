package com.werwolv.game.shader;

import com.werwolv.game.entity.EntityLight;
import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;

import java.util.List;


public class ShaderNormalMapping extends Shader {

    private static final int MAX_LIGHTS = 4;


    private int loc_transformationMatrix;
    private int loc_projectionMatrix;
    private int loc_viewMatrix;
    private int loc_lightPositionEyeSpace[];
    private int loc_lightColor[];
    private int loc_attenuation[];
    private int loc_shineDamper;
    private int loc_reflectivity;
    private int loc_skyColor;
    private int loc_numberOfRows;
    private int loc_offset;
    private int loc_plane;
    private int loc_modelSampler;
    private int loc_normalSampler;
    private int loc_extraInfoMapSampler;
    private int loc_usesExtraInfoMap;

    public ShaderNormalMapping() {
        super("shaderNormalMap", "shaderNormalMap");
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
        super.bindAttribute(2, "normal");
        super.bindAttribute(3, "tangent");
    }

    @Override
    public void getAllUniformLocations() {
        loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
        loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
        loc_viewMatrix = super.getUniformLocation("viewMatrix");
        loc_shineDamper = super.getUniformLocation("shineDamper");
        loc_reflectivity = super.getUniformLocation("reflectivity");
        loc_skyColor = super.getUniformLocation("skyColor");
        loc_numberOfRows = super.getUniformLocation("numberOfRows");
        loc_offset = super.getUniformLocation("offset");
        loc_plane = super.getUniformLocation("plane");
        loc_modelSampler = super.getUniformLocation("modelSampler");
        loc_normalSampler = super.getUniformLocation("normalSampler");
        loc_extraInfoMapSampler = super.getUniformLocation("extraInfoMapSampler");
        loc_usesExtraInfoMap = super.getUniformLocation("usesExtraInfoMap");

        loc_lightPositionEyeSpace = new int[MAX_LIGHTS];
        loc_lightColor = new int[MAX_LIGHTS];
        loc_attenuation = new int[MAX_LIGHTS];
        for (int i = 0; i < MAX_LIGHTS; i++) {
            loc_lightPositionEyeSpace[i] = super.getUniformLocation("lightPositionEyeSpace[" + i + "]");
            loc_lightColor[i] = super.getUniformLocation("lightColor[" + i + "]");
            loc_attenuation[i] = super.getUniformLocation("attenuation[" + i + "]");
        }
    }

    public void connectTextureUnits() {
        super.loadInteger(loc_modelSampler, 0);
        super.loadInteger(loc_normalSampler, 1);
        super.loadInteger(loc_extraInfoMapSampler, 2);
    }

    public void loadUseExtraInfoMap(boolean useMap) {
        super.loadBoolean(loc_usesExtraInfoMap, useMap);
    }

    public void loadClipPlane(Vector4f plane) {
        super.loadVector(loc_plane, plane);
    }

    public void loadNumberOfRows(int numberOfRows) {
        super.loadFloat(loc_numberOfRows, numberOfRows);
    }

    public void loadOffset(float x, float y) {
        super.loadVector(loc_offset, new Vector2f(x, y));
    }

    public void loadSkyColor(Vector3f color) {
        super.loadVector(loc_skyColor, color);
    }

    public void loadShineVariables(float damper, float reflectivity) {
        super.loadFloat(loc_shineDamper, damper);
        super.loadFloat(loc_reflectivity, reflectivity);
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_transformationMatrix, matrix);
    }

    public void loadLights(List<EntityLight> lights, Matrix4f viewMatrix) {
        for (int i = 0; i < MAX_LIGHTS; i++) {
            if (i < lights.size()) {
                super.loadVector(loc_lightPositionEyeSpace[i], getEyeSpacePosition(lights.get(i), viewMatrix));
                super.loadVector(loc_lightColor[i], lights.get(i).getColor());
                super.loadVector(loc_attenuation[i], lights.get(i).getAttenuation());
            } else {
                super.loadVector(loc_lightPositionEyeSpace[i], new Vector3f(0, 0, 0));
                super.loadVector(loc_lightColor[i], new Vector3f(0, 0, 0));
                super.loadVector(loc_attenuation[i], new Vector3f(1, 0, 0));
            }
        }
    }

    public void loadViewMatrix(Matrix4f viewMatrix) {
        super.loadMatrix(loc_viewMatrix, viewMatrix);
    }

    public void loadProjectionMatrix(Matrix4f projection) {
        super.loadMatrix(loc_projectionMatrix, projection);
    }

    private Vector3f getEyeSpacePosition(EntityLight light, Matrix4f viewMatrix) {
        Vector3f position = light.getPosition();
        Vector4f eyeSpacePos = new Vector4f(position.x, position.y, position.z, 1f);
        viewMatrix.transform(eyeSpacePos);
        return new Vector3f(eyeSpacePos.x, eyeSpacePos.y, eyeSpacePos.z);
    }


}
