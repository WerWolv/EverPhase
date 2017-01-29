package com.werwolv.game.render;

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
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class RendererParticle {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	
	private ModelRaw quad;
	private ShaderParticle shader;

	public RendererParticle(ResourceLoader loader, Matrix4f projectionMatrix){
		quad = loader.loadToVAO(VERTICES, 2);
		shader = new ShaderParticle();

		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(Map<TextureParticle, List<EntityParticle>> particles, EntityPlayer player){
		Matrix4f viewMatrix = Maths.createViewMatrix(player);

		shader.start();

		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glDepthMask(false);

		for(TextureParticle texture : particles.keySet()) {
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, texture.usesAdditiveBlending() ? GL11.GL_ONE : GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL13.glActiveTexture(GL13.GL_TEXTURE0);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture.getTextureID());
			for (EntityParticle particle : particles.get(texture)) {
				updateModelViewMatrix(particle.getPosition(), particle.getRotZ(), particle.getScale(), viewMatrix);

				shader.loadTextureCoordInfo(particle.getTexOffset1(), particle.getTexOffset2(), texture.getNumOfRows(), particle.getBlend());
				GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCnt());
			}
		}

		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}


	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix) {
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

		shader.loadModelViewMatrix(modelViewMatrix);
	}

	public void clean(){
		shader.clean();
	}

}
