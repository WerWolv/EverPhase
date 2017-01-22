package com.werwolv.game.render.postProcessing;

import com.werwolv.game.main.Main;
import com.werwolv.game.shader.filter.ShaderBright;

public class FilterBright extends PostProcessEffect<ShaderBright>{

	public FilterBright(){
		super(new ShaderBright(), Main.getWindowSize()[0], Main.getWindowSize()[1]);
	}

	@Override
	public void render(int colorTexture, int colorTexture2) {
		super.render(colorTexture, colorTexture2);
	}
}
