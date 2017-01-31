package com.deltabase.everphase.engine.shader.filter;

import com.deltabase.everphase.engine.shader.Shader;

public class ShaderGaussianBlurHorizontal extends Shader {

	private int location_targetWidth;

	public ShaderGaussianBlurHorizontal() {
		super("filter/filterGaussianBlurHorizontal", "filter/filterGaussianBlur");
	}

	public void loadTargetWidth(float width){
		super.loadFloat(location_targetWidth, width);
	}
	
	@Override
	public void getAllUniformLocations() {
		location_targetWidth = super.getUniformLocation("targetWidth");
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
