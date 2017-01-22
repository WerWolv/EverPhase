package com.werwolv.render.postProcessing;

import com.werwolv.main.Main;
import com.werwolv.shader.filter.ShaderGaussianBlurHorizontal;

public class FilterGaussianBlurHorizontal extends PostProcessEffect<ShaderGaussianBlurHorizontal> {

	public FilterGaussianBlurHorizontal(){
		super(new ShaderGaussianBlurHorizontal(), Main.getWindowSize()[0] / 5, Main.getWindowSize()[1] / 5);
		getShader().start();
		getShader().loadTargetWidth(Main.getWindowSize()[0] / 5);
		getShader().stop();
	}

	@Override
	public void render(int colorTexture, int colorTexture2) {
		super.render(colorTexture, colorTexture2);
	}

}
