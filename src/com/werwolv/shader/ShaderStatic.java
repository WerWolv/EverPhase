package com.werwolv.shader;

import com.werwolv.entity.Camera;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;

public class ShaderStatic extends Shader {

    private int loc_transMatrix, loc_projMatrix, loc_viewMatrix;

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
        loc_viewMatrix = super.getUniformLocation("viewMatrix");
    }

    public void loadTransformationMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_transMatrix, matrix);
    }

    public void loadProjectionMatrix(Matrix4f matrix) {
        super.loadMatrix(loc_projMatrix, matrix);
    }
    public void loadViewMatrix(Camera camera) {
        super.loadMatrix(loc_viewMatrix, Maths.createViewMatrix(camera));
    }
}
