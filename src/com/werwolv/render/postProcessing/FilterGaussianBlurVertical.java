package com.werwolv.render.postProcessing;

import com.werwolv.main.Main;
import com.werwolv.shader.filter.ShaderGaussianBlurVertical;

public class FilterGaussianBlurVertical extends PostProcessEffect<ShaderGaussianBlurVertical> {

	public FilterGaussianBlurVertical(){
		super(new ShaderGaussianBlurVertical(), Main.getWindowSize()[0] / 5, Main.getWindowSize()[1] / 5);
		getShader().start();
		getShader().loadTargetHeight(Main.getWindowSize()[1] / 5);
		getShader().stop();
	}

	@Override
	public void render(int colorTexture, int colorTexture2) {
		super.render(colorTexture, colorTexture2);
	}

}
