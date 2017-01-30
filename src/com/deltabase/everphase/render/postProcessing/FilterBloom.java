package com.deltabase.everphase.render.postProcessing;

import com.deltabase.everphase.shader.filter.ShaderBloom;
import com.deltabase.everphase.main.Main;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;

public class FilterBloom extends Filter<ShaderBloom> {
	public FilterBloom() {
		super(new ShaderBloom(), Main.getWindowSize()[0], Main.getWindowSize()[1]);
		getShader().start();
		getShader().connectTextureUnits();
		getShader().stop();
	}

	
	public void render(int colorTexture, int highlightTexture){
		getShader().start();
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, colorTexture);
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, highlightTexture);
		getRenderer().renderQuad();
		getShader().stop();
	}
}
