package com.deltabase.everphase.shader.filter;

import com.deltabase.everphase.shader.Shader;

public class ShaderBright extends Shader {

	private int loc_value;

	public ShaderBright() {
		super("filter/filterBloom", "filter/filterBright");
	}

	@Override
	protected void getAllUniformLocations() {	
		loc_value = super.getUniformLocation("value");
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadValue(float value) {
		super.loadFloat(loc_value, value);
	}

}
