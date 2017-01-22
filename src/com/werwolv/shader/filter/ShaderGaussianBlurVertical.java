package com.werwolv.shader.filter;

import com.werwolv.shader.Shader;

public class ShaderGaussianBlurVertical extends Shader {
	
	private int location_targetHeight;

	public ShaderGaussianBlurVertical() {
		super("filter/filterGaussianBlurVertical", "filter/filterGaussianBlur");
	}

	public void loadTargetHeight(float height){
		super.loadFloat(location_targetHeight, height);
	}

	@Override
	public void getAllUniformLocations() {
		location_targetHeight = super.getUniformLocation("targetHeight");
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
	}
}
