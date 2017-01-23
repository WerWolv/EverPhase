package com.werwolv.game.render.postProcessing;

import com.werwolv.game.main.Main;
import com.werwolv.game.shader.filter.ShaderGaussianBlurHorizontal;

public class FilterGaussianBlurHorizontal extends Filter<ShaderGaussianBlurHorizontal> {

	public FilterGaussianBlurHorizontal(){
		super(new ShaderGaussianBlurHorizontal(), Main.getWindowSize()[0] / 8, Main.getWindowSize()[1] / 8);
		getShader().start();
		getShader().loadTargetWidth(Main.getWindowSize()[0] / 5);
		getShader().stop();
	}

	@Override
	public void render(int colorTexture, int colorTexture2) {
		super.render(colorTexture, colorTexture2);
	}

}
