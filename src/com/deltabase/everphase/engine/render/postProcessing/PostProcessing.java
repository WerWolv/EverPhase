package com.deltabase.everphase.engine.render.postProcessing;

import com.deltabase.everphase.api.EverPhaseApi;
import com.deltabase.everphase.engine.model.ModelRaw;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.ArrayList;
import java.util.List;

public class PostProcessing {

	private static final float[] POSITIONS = {-1.0F, 1.0F, -1.0F, -1.0F, 1.0F, 1.0F, 1.0F, -1.0F};
	private static ModelRaw quad;

	private static List<Filter> postProcessEffects = new ArrayList<>();

	public static void init() {
		quad = EverPhaseApi.RESOURCE_LOADER.loadToVAO(POSITIONS, 2);
	}

	public static void doPostProcessing(int colorTexture){
		start();
		postProcessEffects.get(0).render(colorTexture, 0);

		for(int effect = 1; effect < postProcessEffects.size(); effect++)
			postProcessEffects.get(effect).render(postProcessEffects.get(effect - 1).getOutputTexture(), colorTexture);

		end();
	}
	
	public static void clean(){
		for(Filter effect : postProcessEffects)
			effect.clean();

		postProcessEffects.clear();
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

	public static void applyEffect(Filter effect) {
		postProcessEffects.add(effect);
	}

}
