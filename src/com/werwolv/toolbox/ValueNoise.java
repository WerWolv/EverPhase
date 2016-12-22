package com.werwolv.toolbox;

import java.util.Random;

public class ValueNoise{
	
	public final int width;
	public final int height;
	
	private float[][] heightMap;
	private int[][] Map;
	public int octaves = 8;
	public int startFrequencyX = 2;
	public int startFrequencyY = 2;
	public float alpha = 20;
	
	public ValueNoise(int width, int height){
		this.width = width;
		this.height = height;
		heightMap = new float[width][height];
		Map = new int[width][height];
		calculate();
	}
	
	public void calculate(){
		int currentFrequencyX = startFrequencyX;
		int currentFrequencyY = startFrequencyY;
		float currentAlpha = alpha;
		
		for(int oc = 0; oc < octaves; oc++){
			if(oc > 0){
				currentFrequencyX *= 2;
				currentFrequencyY *= 2;
				currentAlpha /= 2;
			}
			
			float[][] discretePoints = new float[currentFrequencyX + 1][currentFrequencyY + 1];
			for(int i = 0; i < currentFrequencyX + 1; i++){
				for(int k = 0; k < currentFrequencyY + 1; k++){
					discretePoints[i][k] = (new Random().nextFloat() * currentAlpha * 2) - currentAlpha;
				}
			}
			
			for(int i = 0; i < width; i++){
				for(int k = 0; k < height; k++){
					float currentX = i / (float)width * currentFrequencyX;
					float currentY = k / (float)height * currentFrequencyY;
					int indexX = (int)currentX;
					int indexY = (int)currentY;
					
					float w0 = interpolate(discretePoints[indexX][indexY], discretePoints[indexX + 1][indexY], currentX - indexX);
					float w1 = interpolate(discretePoints[indexX][indexY + 1], discretePoints[indexX + 1][indexY + 1], currentX - indexX);
					float w = interpolate(w0, w1, currentY - indexY);
					heightMap[i][k] += w;
				}
			}
		}
		normalize();
		
		for(int i = 0; i < width; i++) {
			for (int k = 0; k < height; k++) {
				if (heightMap[i][k] > 0.5) Map[i][k] = 1;
				else Map[i][k] = 0;
			}
		}
	}
	
	private void normalize(){
		float min = Float.MAX_VALUE;
		for(int i = 0; i < width; i++){
			for(int k = 0; k < height; k++){
				if(heightMap[i][k] < min){
					min = heightMap[i][k];
				}
			}
		}
		for(int i = 0; i < width; i++){
			for(int k = 0; k < height; k++){
				heightMap[i][k] -= min;
			}
		}
		
		float max = Float.MIN_VALUE;
		for(int i = 0; i < width; i++){
			for(int k = 0; k < height; k++){
				if(heightMap[i][k] > max){
					max = heightMap[i][k];
				}
			}
		}
		for(int i = 0; i < width; i++){
			for(int k = 0; k < height; k++){
				heightMap[i][k] /= max;
			}
		}
	}
	
	private float interpolate(float a, float b, float t){
		float t2 = (float)(1- Math.cos(t * Math.PI)) / 2;
		return (a*(1 - t2) + b * t2);
	}
	
	public float[][] getHeightmap(){
		return heightMap;
	}
}