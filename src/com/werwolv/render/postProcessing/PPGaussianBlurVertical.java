package com.werwolv.render.postProcessing;

import com.werwolv.main.Main;
import com.werwolv.render.RendererImage;
import com.werwolv.shader.ShaderGaussianBlurVertical;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class PPGaussianBlurVertical extends PostProcessEffect<ShaderGaussianBlurVertical> {

	public PPGaussianBlurVertical(){
		super(new ShaderGaussianBlurVertical(), Main.getWindowSize()[0], Main.getWindowSize()[1]);
		getShader().start();
		getShader().loadTargetHeight(Main.getWindowSize()[1]);
		getShader().stop();
	}

	@Override
	public void render(int texture) {
		super.render(texture);
	}

}
