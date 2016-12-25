package com.werwolv.shader;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class ShaderSkybox extends Shader {

    private int loc_projectionMatrix;
    private int loc_viewMatrix;
    private int loc_fogColor;

    public ShaderSkybox() {
        super("shaderSkybox", "shaderSkybox");
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_projectionMatrix, matrix);
    }

    public void loadViewMatrix(EntityPlayer player) {
        Matrix4f matrix = Maths.createViewMatrix(player);
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);

        super.loadMatrix(loc_viewMatrix, matrix);
    }

    public void loadFogColor(float r, float g, float b) {
        super.loadVector(loc_fogColor, new Vector3f(r, g, b));
    }

    @Override
    protected void getAllUniformLocations() {
        loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
        loc_viewMatrix = super.getUniformLocation("viewMatrix");
        loc_fogColor = super.getUniformLocation("fogColor");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
