package com.deltabase.everphase.render.postProcessing;

import com.deltabase.everphase.main.Main;
import com.deltabase.everphase.shader.filter.ShaderGaussianBlurVertical;

public class FilterGaussianBlurVertical extends Filter<ShaderGaussianBlurVertical> {

	public FilterGaussianBlurVertical(){
		super(new ShaderGaussianBlurVertical(), Main.getWindowSize()[0] / 8, Main.getWindowSize()[1] / 8);
		getShader().start();
		getShader().loadTargetHeight(Main.getWindowSize()[1] / 5);
		getShader().stop();
	}

	@Override
	public void render(int colorTexture, int colorTexture2) {
		super.render(colorTexture, colorTexture2);
	}

}
