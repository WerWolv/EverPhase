package com.deltabase.everphase.engine.shader.filter;

import com.deltabase.everphase.engine.shader.Shader;

public class ShaderBloom extends Shader {

	private int loc_colorTexture;
	private int loc_highlightTexture;
	
	public ShaderBloom() {
		super("filter/filterBloom", "filter/filterBloom");
	}
	
	@Override
	protected void getAllUniformLocations() {
		loc_colorTexture = super.getUniformLocation("colorTexture");
		loc_highlightTexture = super.getUniformLocation("highlightTexture");
	}
	
	public void connectTextureUnits(){
		super.loadInteger(loc_colorTexture, 0);
		super.loadInteger(loc_highlightTexture, 1);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
