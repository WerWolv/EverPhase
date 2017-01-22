package com.werwolv.shader;

public class ShaderContrast extends Shader {
	
	public ShaderContrast() {
		super("postProcessing/ppContrast", "postProcessing/ppContrast");
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
