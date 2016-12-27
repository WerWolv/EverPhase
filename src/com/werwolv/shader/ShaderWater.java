package com.werwolv.shader;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;

public class ShaderWater extends Shader {

	private int loc_modelMatrix;
	private int loc_viewMatrix;
	private int loc_projectionMatrix;
	private int loc_reflectionTexture, loc_refractionTexture;

	public ShaderWater() {
		super("shaderWater", "shaderWater");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		loc_projectionMatrix = getUniformLocation("projectionMatrix");
		loc_viewMatrix = getUniformLocation("viewMatrix");
		loc_modelMatrix = getUniformLocation("modelMatrix");

		loc_reflectionTexture = getUniformLocation("reflectionTexture");
		loc_refractionTexture = getUniformLocation("refractionTexture");
	}

	public void connectsTextureUnits() {
		super.loadInteger(loc_reflectionTexture, 0);
		super.loadInteger(loc_refractionTexture, 1);
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(loc_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(EntityPlayer player){
		Matrix4f viewMatrix = Maths.createViewMatrix(player);
		loadMatrix(loc_viewMatrix, viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(loc_modelMatrix, modelMatrix);
	}

}
