package com.deltabase.everphase.engine.shader;

import org.joml.Matrix4f;

public class ShaderParticle extends Shader {

	private int loc_projectionMatrix;
	private int loc_numOfRows;

	public ShaderParticle() {
		super("shaderParticle", "shaderParticle");
	}

	@Override
	public void getAllUniformLocations() {
		loc_numOfRows = super.getUniformLocation("numOfRows");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");
	}

	@Override
	public void bindAttributes() {
		super.bindAttribute(0, "position");
		super.bindAttribute(1, "modelViewMatrix");
		super.bindAttribute(5, "texOffsets");
		super.bindAttribute(6, "blendFactor");

	}


	public void loadProjectionMatrix(Matrix4f projectionMatrix) {
		super.loadMatrix(loc_projectionMatrix, projectionMatrix);
	}

	public void loadNumOfRows(float numOfRows) {
		super.loadFloat(loc_numOfRows, numOfRows);
	}

}
