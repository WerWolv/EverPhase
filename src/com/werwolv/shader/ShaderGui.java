package com.werwolv.shader;

import org.joml.Matrix4f;

public class ShaderGui extends Shader{

    private int loc_transformationMatrix;

    public ShaderGui() {
        super("shaderGui", "shaderGui");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_transformationMatrix, matrix);
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
    }

    @Override
    protected void getAllUniformLocations() {
        loc_transformationMatrix = super.getUniformLocation("transformationMatrix");
    }
}
