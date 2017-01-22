package com.werwolv.render.postProcessing;

import com.werwolv.model.ModelRaw;
import com.werwolv.modelloader.ResourceLoader;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class PostProcessing {
	
	private static final float[] POSITIONS = { -1, 1, -1, -1, 1, 1, 1, -1 };	
	private static ModelRaw quad;
	private static PPContrast ppContrast;

	public static void init(ResourceLoader loader){
		quad = loader.loadToVAO(POSITIONS, 2);
		ppContrast = new PPContrast();
	}
	
	public static void doPostProcessing(int colourTexture){
		start();
		ppContrast.render(colourTexture);
		end();
	}
	
	public static void cleanUp(){
		ppContrast.clean();
	}
	
	private static void start(){
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
	
	private static void end(){
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
	}


}
