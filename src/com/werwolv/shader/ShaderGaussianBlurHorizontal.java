package com.werwolv.shader;

public class ShaderGaussianBlurHorizontal extends Shader {

	private int location_targetWidth;

	public ShaderGaussianBlurHorizontal() {
		super("postProcessing/ppGaussianBlurHorizontal", "postProcessing/ppGaussianBlur");
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
