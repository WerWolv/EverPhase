package com.werwolv.shader;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;

public class ShaderWater extends Shader {

	private int location_modelMatrix;
	private int location_viewMatrix;
	private int location_projectionMatrix;

	public ShaderWater() {
		super("shaderWater", "shaderWater");
	}

	@Override
	protected void bindAttributes() {
		bindAttribute(0, "position");
	}

	@Override
	protected void getAllUniformLocations() {
		location_projectionMatrix = getUniformLocation("projectionMatrix");
		location_viewMatrix = getUniformLocation("viewMatrix");
		location_modelMatrix = getUniformLocation("modelMatrix");
	}

	public void loadProjectionMatrix(Matrix4f projection) {
		loadMatrix(location_projectionMatrix, projection);
	}
	
	public void loadViewMatrix(EntityPlayer player){
		Matrix4f viewMatrix = Maths.createViewMatrix(player);
		loadMatrix(location_viewMatrix, viewMatrix);
	}

	public void loadModelMatrix(Matrix4f modelMatrix){
		loadMatrix(location_modelMatrix, modelMatrix);
	}

}
