package com.deltabase.everphase.engine.render;

import com.deltabase.everphase.engine.fbo.FrameBufferObject;
import org.lwjgl.opengl.GL11;

public class RendererImage {

	private FrameBufferObject fbo;

	public RendererImage(int width, int height) {
		this.fbo = new FrameBufferObject(width, height, FrameBufferObject.NONE);
	}

	public RendererImage() {

	}

	public void renderQuad() {
		if (fbo != null) {
			fbo.bindFrameBuffer();
		}
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
		GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, 4);
		if (fbo != null) {
			fbo.unbindFrameBuffer();
		}
	}

	public int getOutputTexture() {
		return fbo.getColorTexture();
	}

	public void clean() {
		if (fbo != null) {
			fbo.clean();
		}
	}

}
