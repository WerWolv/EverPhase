package com.werwolv.shader;

import org.joml.Matrix4f;

public class ShaderShadow extends Shader {

	
	private int location_mvpMatrix;

	public ShaderShadow() {
		super("shaderShadow", "shaderShadow");
	}

	@Override
	protected void getAllUniformLocations() {
		location_mvpMatrix = super.getUniformLocation("mvpMatrix");
		
	}
	
	public void loadMvpMatrix(Matrix4f mvpMatrix){
		super.loadMatrix(location_mvpMatrix, mvpMatrix);
	}

	@Override
	protected void bindAttributes() {
		super.bindAttribute(0, "in_position");
		super.bindAttribute(1, "in_textureCoords");
	}

}
