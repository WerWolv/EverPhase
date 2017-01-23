package com.werwolv.game.render;

import com.werwolv.game.entity.EntityLight;
import com.werwolv.game.entity.EntityPlayer;
import com.werwolv.game.fbo.FrameBufferObject;
import com.werwolv.game.main.Main;
import com.werwolv.game.model.ModelRaw;
import com.werwolv.game.modelloader.ResourceLoader;
import com.werwolv.game.shader.ShaderWater;
import com.werwolv.game.terrain.TileWater;
import com.werwolv.game.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class RendererWater {

	private static final float WAVE_SPEED = 0.03F;

	private ModelRaw quad;
	private ShaderWater shader;
	private FrameBufferObject fboReflection;
	private FrameBufferObject fboRefraction;
	private float moveFactor = 0;

	private int textureIdDuDvMap, textureIdNormalMap;

	public RendererWater(ResourceLoader loader, Matrix4f projectionMatrix, float nearPlane, float farPlane) {
		this.shader = new ShaderWater();
		fboReflection = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1], FrameBufferObject.DEPTH_TEXTURE);
		fboRefraction = new FrameBufferObject(Main.getWindowSize()[0], Main.getWindowSize()[1], FrameBufferObject.DEPTH_TEXTURE);

		textureIdDuDvMap = loader.loadTexture("dudvMapWater");
		textureIdNormalMap = loader.loadTexture("normalMap");

		shader.start();
		shader.loadNearAndFarPlane(nearPlane, farPlane);
		shader.connectsTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<TileWater> waters, List<EntityLight> lights, EntityPlayer player) {
		shader.start();
		prepareRender(player, lights);
		renderWithoutEffects(waters);
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
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fboReflection.getColorTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fboRefraction.getColorTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE2);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIdDuDvMap);
		GL13.glActiveTexture(GL13.GL_TEXTURE3);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureIdNormalMap);
		GL13.glActiveTexture(GL13.GL_TEXTURE4);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fboRefraction.getDepthTexture());

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	}
	
	private void unbind(){
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

	private void setUpVAO(ResourceLoader loader) {
		float[] vertices = { -1, -1, -1, 1, 1, -1, 1, -1, -1, 1, 1, 1 };
		quad = loader.loadToVAO(vertices, 2);
	}

	public void clean() {
		shader.clean();
	}

	public FrameBufferObject getFboReflection() {
		return fboReflection;
	}

	public FrameBufferObject getFboRefraction() {
		return fboRefraction;
	}
}
