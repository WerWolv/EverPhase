package com.werwolv.game.shader.filter;

import com.werwolv.game.shader.Shader;

public class ShaderBloom extends Shader {

	private int loc_colourTexture;
	private int loc_highlightTexture;
	
	public ShaderBloom() {
		super("filter/filterBloom", "filter/filterBloom");
	}
	
	@Override
	protected void getAllUniformLocations() {
		loc_colourTexture = super.getUniformLocation("colourTexture");
		loc_highlightTexture = super.getUniformLocation("highlightTexture");
	}
	
	public void connectTextureUnits(){
		super.loadInteger(loc_colourTexture, 0);
		super.loadInteger(loc_highlightTexture, 1);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}
	
}
