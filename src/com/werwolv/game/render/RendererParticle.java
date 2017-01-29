package com.werwolv.game.render;

import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import com.werwolv.game.entity.EntityPlayer;
import com.werwolv.game.entity.particle.EntityParticle;
import com.werwolv.game.model.ModelRaw;
import com.werwolv.game.modelloader.ResourceLoader;
import com.werwolv.game.resource.TextureParticle;
import com.werwolv.game.shader.ShaderParticle;
import com.werwolv.game.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

public class RendererParticle {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	private static final int MAX_PARTICLES_IN_SCENE = 65536;
	private static final int INSTANCED_DATA_LENGTH = 21;

	private static final FloatBuffer buffer = BufferUtils.createFloatBuffer(MAX_PARTICLES_IN_SCENE * INSTANCED_DATA_LENGTH);

	private ModelRaw quad;
	private ShaderParticle shader;
	private ResourceLoader loader;
	private int vboID;
	private int floatArraypointer = 0;

	public RendererParticle(ResourceLoader loader, Matrix4f projectionMatrix){
		quad = loader.loadToVAO(VERTICES, 2);
		shader = new ShaderParticle();
		this.loader = loader;

		this.vboID = loader.createEmptyVBO(INSTANCED_DATA_LENGTH * MAX_PARTICLES_IN_SCENE);
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 1, 4, INSTANCED_DATA_LENGTH, 0);
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 2, 4, INSTANCED_DATA_LENGTH, 4);
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 3, 4, INSTANCED_DATA_LENGTH, 8);
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 4, 4, INSTANCED_DATA_LENGTH, 12);
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 5, 4, INSTANCED_DATA_LENGTH, 16);
		loader.addInstancedAttribute(quad.getVaoID(), vboID, 6, 1, INSTANCED_DATA_LENGTH, 20);


		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<TextureParticle, List<EntityParticle>> particles, EntityPlayer player){
		Matrix4f viewMatrix = Maths.createViewMatrix(player);

		shader.start();

		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL20.glEnableVertexAttribArray(1);
		GL20.glEnableVertexAttribArray(2);
		GL20.glEnableVertexAttribArray(3);
		GL20.glEnableVertexAttribArray(4);
		GL20.glEnableVertexAttribArray(5);
		GL20.glEnableVertexAttribArray(6);

		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);

		for(TextureParticle texture : particles.keySet()) {
			List<EntityParticle> particleList = particles.get(texture);

			bindTexture(texture);
			floatArraypointer = 0;
			float[] vboData = new float[particleList.size() * INSTANCED_DATA_LENGTH];

			for (EntityParticle particle :particleList) {
				updateModelViewMatrix(particle.getPosition(), particle.getRotZ(), particle.getScale(), viewMatrix, vboData);
				updateTexCoordInfo(particle, vboData);
			}
			loader.updateVBO(vboID, vboData, buffer);
			GL31.glDrawArraysInstanced(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCnt(), particleList.size());
		}

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL20.glDisableVertexAttribArray(1);
		GL20.glDisableVertexAttribArray(2);
		GL20.glDisableVertexAttribArray(3);
		GL20.glDisableVertexAttribArray(4);
		GL20.glDisableVertexAttribArray(5);
		GL20.glDisableVertexAttribArray(6);
		GL30.glBindVertexArray(0);
		shader.stop();
	}


	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix, float[] vboData) {
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix = modelMatrix.translate(position, new Matrix4f());

		modelMatrix.m00(viewMatrix.m00());
		modelMatrix.m01(viewMatrix.m10());
		modelMatrix.m02(viewMatrix.m20());
		modelMatrix.m10(viewMatrix.m01());
		modelMatrix.m11(viewMatrix.m11());
		modelMatrix.m12(viewMatrix.m21());
		modelMatrix.m20(viewMatrix.m02());
		modelMatrix.m21(viewMatrix.m12());
		modelMatrix.m22(viewMatrix.m22());

		modelMatrix = modelMatrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1), new Matrix4f());
		modelMatrix = modelMatrix.scale(new Vector3f(scale, scale, scale), new Matrix4f());

		Matrix4f modelViewMatrix = viewMatrix.mul(modelMatrix, new Matrix4f());
		storeMatrixData(modelViewMatrix, vboData);
	}

	private void storeMatrixData(Matrix4f matrix, float[] vboData) {
		vboData[floatArraypointer++] = matrix.m00();
		vboData[floatArraypointer++] = matrix.m01();
		vboData[floatArraypointer++] = matrix.m02();
		vboData[floatArraypointer++] = matrix.m03();
		vboData[floatArraypointer++] = matrix.m10();
		vboData[floatArraypointer++] = matrix.m11();
		vboData[floatArraypointer++] = matrix.m12();
		vboData[floatArraypointer++] = matrix.m13();
		vboData[floatArraypointer++] = matrix.m20();
		vboData[floatArraypointer++] = matrix.m21();
		vboData[floatArraypointer++] = matrix.m22();
		vboData[floatArraypointer++] = matrix.m23();
		vboData[floatArraypointer++] = matrix.m30();
		vboData[floatArraypointer++] = matrix.m31();
		vboData[floatArraypointer++] = matrix.m32();
		vboData[floatArraypointer++] = matrix.m33();
	}

	private void updateTexCoordInfo(EntityParticle particle, float[] data) {
		data[floatArraypointer++] = particle.getTexOffset1().x;
		data[floatArraypointer++] = particle.getTexOffset1().y;
		data[floatArraypointer++] = particle.getTexOffset2().x;
		data[floatArraypointer++] = particle.getTexOffset2().y;
		data[floatArraypointer++] = particle.getBlend();

	}

	private void bindTexture(TextureParticle texture) {
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, texture.usesAdditiveBlending() ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL13.glActiveTexture(GL13.GL_TEXTURE0);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
		shader.loadNumOfRows(texture.getNumOfRows());
	}

	public void clean(){
		shader.clean();
	}

}
