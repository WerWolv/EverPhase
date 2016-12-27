package com.werwolv.render;

import java.util.List;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.fbo.FrameBufferWater;
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

	private ModelRaw quad;
	private ShaderWater shader;
	private FrameBufferWater fboWater;

	public RendererWater(ModelLoader loader, Matrix4f projectionMatrix, FrameBufferWater fboWater) {
		this.shader = new ShaderWater();
		this.fboWater = fboWater;

		shader.start();
		shader.connectsTextureUnits();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
		setUpVAO(loader);
	}

	public void render(List<TileWater> water, EntityPlayer player) {
		prepareRender(player);
		for (TileWater tile : water) {
			Matrix4f modelMatrix = Maths.createTransformationMatrix(
					new Vector3f(tile.getX(), tile.getHeight(), tile.getZ()), 0, 0, 0,
					TileWater.TILE_SIZE);
			shader.loadModelMatrix(modelMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, quad.getVertexCnt());
		}
		unbind();
	}
	
	private void prepareRender(EntityPlayer player){
		shader.start();
		shader.loadViewMatrix(player);
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fboWater.getReflectionTexture());
		GL13.glActiveTexture(GL13.GL_TEXTURE1);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, fboWater.getRefractionTexture());
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
