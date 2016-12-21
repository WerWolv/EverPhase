package com.werwolv.shader;

import org.joml.Matrix4f;

public class ShaderStatic extends Shader {

    private int loc_transMatrix, loc_projMatrix;

    public ShaderStatic() {
        super("vertexShader", "fragmentShader");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }

    @Override
    protected void getAllUniformLocations() {
        loc_transMatrix = super.getUniformLocation("transformationMatrix");
        loc_projMatrix = super.getUniformLocation("projectionMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_transMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_projMatrix, matrix);
    }
}
