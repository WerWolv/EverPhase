package com.werwolv.shader;

import org.joml.Matrix4f;

public class ShaderShadow extends Shader {

    private int loc_mvpMatrix;

    public ShaderShadow() {
        super("shaderShadow", "shaderShadow");
    }

    @Override
    public void getAllUniformLocations() {
        loc_mvpMatrix = super.getUniformLocation("mvpMatrix");

    }

    public void loadMvpMatrix(Matrix4f mvpMatrix) {
        super.loadMatrix(loc_mvpMatrix, mvpMatrix);
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "in_position");
    }

}
