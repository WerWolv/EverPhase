package com.werwolv.game.shader;

import org.joml.Matrix4f;

public class ShaderParticle extends Shader {

	private int loc_modelViewMatrix;
	private int loc_projectionMatrix;

	public ShaderParticle() {
		super("shaderParticle", "shaderParticle");
	}

	@Override
	public void getAllUniformLocations() {
		loc_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(loc_modelViewMatrix, modelView);
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(loc_projectionMatrix, projectionMatrix);
	}

}
