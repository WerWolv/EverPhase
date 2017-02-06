package com.deltabase.everphase.engine.shader;

import org.joml.Matrix4f;
import org.joml.Vector4f;

public class ShaderGui extends Shader{

    private int loc_transformationMatrix;
    private int loc_size;
    private int loc_overlay;
    private int loc_overlayColor;

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
        loc_size = super.getUniformLocation("size");
        loc_overlay = super.getUniformLocation("overlay");
        loc_overlayColor = super.getUniformLocation("overlayColor");
    }

    public void loadSize(float startX, float startY, float endX, float endY) {
        super.loadVector(loc_size, new Vector4f(startX, startY, endX, endY));
    }

    public void loadOverlay(boolean enabled) {
        super.loadBoolean(loc_overlay, enabled);
    }

    public void loadOverlayColor(Vector4f color) {
        super.loadVector(loc_overlayColor, color);
    }
}
