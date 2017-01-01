package com.werwolv.render;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.entity.particle.EntityParticle;
import com.werwolv.model.ModelRaw;
import com.werwolv.modelloader.ModelLoader;
import com.werwolv.shader.ShaderParticle;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

import java.util.List;

public class RendererParticle {
	
	private static final float[] VERTICES = {-0.5f, 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, -0.5f};
	
	private ModelRaw quad;
	private ShaderParticle shader;
	
	public RendererParticle(ModelLoader loader, Matrix4f projectionMatrix){
		quad = loader.loadToVAO(VERTICES, 2);
		shader = new ShaderParticle();

		shader.start();
		shader.loadProjectionMatrix(projectionMatrix);
		shader.stop();
	}

	public void render(List<EntityParticle> particles, EntityPlayer player){
		Matrix4f viewMatrix = Maths.createViewMatrix(player);
		prepare();

		for(EntityParticle particle : particles) {
			updateModelViewMatrix(particle.getPosition(), particle.getRotation(), particle.getScale(), viewMatrix);
			GL11.glDrawArrays(GL11.GL_TRIANGLE_STRIP, 0, quad.getVertexCnt());
		}

		finishRendering();
	}

	private void updateModelViewMatrix(Vector3f position, float rotation, float scale, Matrix4f viewMatrix) {
		Matrix4f modelMatrix = new Matrix4f();
		modelMatrix.transpose3x3();

		modelMatrix.translate(position);

		modelMatrix.rotate((float) Math.toRadians(rotation), new Vector3f(0, 0, 1));
		modelMatrix.scale(new Vector3f(scale, scale, scale));
		shader.loadModelViewMatrix(viewMatrix.mul(modelMatrix));
	}

	public void clean(){
		shader.clean();
	}
	
	private void prepare(){
		shader.start();
		GL30.glBindVertexArray(quad.getVaoID());
		GL20.glEnableVertexAttribArray(0);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glDepthMask(false);
	}
	
	private void finishRendering(){
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
		GL20.glDisableVertexAttribArray(0);
		GL30.glBindVertexArray(0);
		shader.stop();
	}

}
