package com.werwolv.shader;

public class ShaderContrast extends Shader {

	private int loc_contrast;

	public ShaderContrast() {
		super("postProcessing/ppContrast", "postProcessing/ppContrast");
	}

	@Override
	protected void getAllUniformLocations() {
		this.loc_contrast = super.getUniformLocation("contrast");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadContrast(float contrast) {
		super.loadFloat(loc_contrast, contrast);
	}

}
