package com.werwolv.render.postProcessing;

import com.werwolv.main.Main;
import com.werwolv.render.RendererImage;
import com.werwolv.shader.ShaderGaussianBlurHorizontal;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class PPGaussianBlurHorizontal extends PostProcessEffect<ShaderGaussianBlurHorizontal> {

	public PPGaussianBlurHorizontal(){
		super(new ShaderGaussianBlurHorizontal(), Main.getWindowSize()[0], Main.getWindowSize()[1]);
		getShader().start();
		getShader().loadTargetWidth(Main.getWindowSize()[0]);
		getShader().stop();
	}

	@Override
	public void render(int texture) {
		super.render(texture);
	}

}
