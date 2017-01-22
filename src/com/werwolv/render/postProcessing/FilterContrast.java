package com.werwolv.render.postProcessing;

import com.werwolv.shader.filter.ShaderContrast;

public class FilterContrast extends PostProcessEffect<ShaderContrast> {

    private float contrast;

    public FilterContrast(float contrast) {
        super(new ShaderContrast());

        this.contrast = contrast;
    }

    @Override
    public void render(int colorTexture, int colorTexture2) {
        super.render(colorTexture, colorTexture2);

        this.getShader().start();
        this.getShader().loadContrast(contrast);
        this.getShader().stop();
    }
}
