package com.werwolv.shader;

import com.werwolv.entity.EntityPlayer;
import com.werwolv.toolbox.Maths;
import org.joml.Matrix4f;

public class ShaderWater extends Shader {

	private int loc_modelMatrix;
	private int loc_viewMatrix;
	private int loc_projectionMatrix;

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
