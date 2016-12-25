package com.werwolv.shader;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;

public class ShaderSkybox extends Shader {

    private int location_projectionMatrix;
    private int location_viewMatrix;

    public ShaderSkybox() {
        super("shaderSkybox", "shaderSkybox");
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(location_projectionMatrix, matrix);
    }

    public void loadViewMatrix(EntityPlayer player) {
        Matrix4f matrix = Maths.createViewMatrix(player);
        matrix.m30(0);
        matrix.m31(0);
        matrix.m32(0);

        super.loadMatrix(location_viewMatrix, matrix);
    }

    @Override
    protected void getAllUniformLocations() {
        location_projectionMatrix = super.getUniformLocation("projectionMatrix");
        location_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }
}
