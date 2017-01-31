package com.deltabase.everphase.engine.shader.filter;

import com.deltabase.everphase.engine.shader.Shader;
import org.joml.Vector2f;

public class ShaderVignette extends Shader {

    private int loc_resolution, loc_vignetteTex;

    public ShaderVignette() {
        super("filter/filterVignette", "filter/filterVignette");
    }

    @Override
    public void bindAttributes() {
        super.bindAttribute(0, "position");

    }

    @Override
    public void connectTextureUnits() {
        super.loadInteger(loc_vignetteTex, 0);
    }

    @Override
    public void getAllUniformLocations() {
        loc_resolution = super.getUniformLocation("resolution");
        loc_vignetteTex = super.getUniformLocation("vignetteTex");
    }

    public void loadResolution(int width, int height) {
        super.loadVector(loc_resolution, new Vector2f(width, height));
    }
}
