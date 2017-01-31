package com.deltabase.everphase.engine.render.postProcessing;

import com.deltabase.everphase.engine.shader.filter.ShaderBright;
import com.deltabase.everphase.main.Main;

public class FilterBright extends Filter<ShaderBright> {

	public FilterBright(float value){
		super(new ShaderBright(), Main.getWindowSize()[0], Main.getWindowSize()[1]);

		getShader().start();
		getShader().loadValue(value);
		getShader().stop();
	}

	@Override
	public void render(int colorTexture, int colorTexture2) {
		super.render(colorTexture, colorTexture2);
	}
}
