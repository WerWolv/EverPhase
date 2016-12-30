package com.werwolv.shader;

import org.joml.Matrix4f;

public class ShaderParticle extends Shader {

	private int location_modelViewMatrix;
	private int location_projectionMatrix;

	public ShaderParticle() {
		super("shaderParticle", "shaderParticle");
	}

	@Override
	public void getAllUniformLocations() {
		location_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		location_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
	}

	public void loadModelViewMatrix(Matrix4f modelView) {
		super.loadMatrix(location_modelViewMatrix, modelView);
	}

	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(location_projectionMatrix, projectionMatrix);
	}

}
