package com.werwolv.game.shader;

import org.joml.Matrix4f;
import org.joml.Vector2f;

public class ShaderParticle extends Shader {

	private int loc_modelViewMatrix;
	private int loc_projectionMatrix;

	private int loc_texOffset1, loc_texOffset2;
	private int loc_texCoordInfo;

	public ShaderParticle() {
		super("shaderParticle", "shaderParticle");
	}

	@Override
	public void getAllUniformLocations() {
		loc_modelViewMatrix = super.getUniformLocation("modelViewMatrix");
		loc_projectionMatrix = super.getUniformLocation("projectionMatrix");

		loc_texOffset1 = super.getUniformLocation("texOffset1");
		loc_texOffset2 = super.getUniformLocation("texOffset2");
		loc_texCoordInfo = super.getUniformLocation("texCoordInfo");
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

	public void loadTextureCoordInfo(Vector2f offset1, Vector2f offset2, float numOfRows, float blend) {
		super.loadVector(loc_texOffset1, offset1);
		super.loadVector(loc_texOffset2, offset2);
		super.loadVector(loc_texCoordInfo, new Vector2f(numOfRows, blend));
	}

}
