package com.werwolv.render;

import java.util.List;

import com.werwolv.entity.EntityLight;
import com.werwolv.entity.EntityPlayer;
import com.werwolv.fbo.FrameBufferWater;
import com.werwolv.main.Main;
import com.werwolv.model.ModelRaw;
import com.werwolv.shader.ShaderWater;

import com.werwolv.terrain.TileWater;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RendererWater {

	private static final float WAVE_SPEED = 0.03F;

	private ModelRaw quad;
	private ShaderWater shader;
	private FrameBufferWater fboWater;

	private float moveFactor = 0;

	private int textureIdDuDvMap, textureIdNormalMap;

	public RendererWater(ModelLoader loader, Matrix4f projectionMatrix, FrameBufferWater fboWater) {
		this.shader = new ShaderWater();
		this.fboWater = fboWater;

		textureIdDuDvMap = loader.loadTexture("dudvMapWater");
		textureIdNormalMap = loader.loadTexture("normalMap");

		shader.start();
		shader.connectsTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<TileWater> water, List<EntityLight> lights, EntityPlayer player) {
		shader.start();
		prepareRender(player, lights);
		renderWithoutEffects(water);
		unbind();
	}

	public void renderWithoutEffects(List<TileWater> water) {
		for (TileWater tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0, TileWater.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCnt());
		}
	}
	
	private void prepareRender(EntityPlayer player, List<EntityLight> lights){
		shader.loadViewMatrix(player);

		moveFactor += WAVE_SPEED * Main.getFrameTimeSeconds();
		moveFactor %= 1;
		shader.loadMoveFactor(moveFactor);
		shader.loadLight(lights);

		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fboWater.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fboWater.getRefractionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIdDuDvMap);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIdNormalMap);
	}
	
	private void unbind(){
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(ModelLoader loader) {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

	public void clean() {
		shader.clean();
	}

}
