package com.werwolv.game.shader.filter;

import com.werwolv.game.shader.Shader;

public class ShaderBright extends Shader {

	public ShaderBright() {
		super("filter/filterBloom", "filter/filterBright");
	}

	@Override
	protected void getAllUniformLocations() {	
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

}
