package com.deltabase.everphase.shader;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.FloatBuffer;

public abstract class Shader {

    private int programID;
    private int vertexShaderID;
    private int fragmentShaderID;

    private static FloatBuffer buffer = BufferUtils.createFloatBuffer(16);


    public Shader(String vertexFile, String fragmentFile) {
        vertexShaderID = loadShader("shaders/" + vertexFile + ".vert", GL20.GL_VERTEX_SHADER);
        fragmentShaderID = loadShader("shaders/" + fragmentFile + ".frag", GL20.GL_FRAGMENT_SHADER);

        programID = GL20.glCreateProgram();
        GL20.glAttachShader(programID, vertexShaderID);
        GL20.glAttachShader(programID, fragmentShaderID);
        bindAttributes();
        GL20.glLinkProgram(programID);
        GL20.glValidateProgram(programID);
        getAllUniformLocations();
    }

    public void connectTextureUnits() {

    }

    protected abstract void bindAttributes();

    protected abstract void getAllUniformLocations();

    protected int getUniformLocation(String uniformName) {
        return GL20.glGetUniformLocation(programID, uniformName);
    }

    protected void bindAttribute(int attribute, String varName) {
        GL20.glBindAttribLocation(programID, attribute, varName);
    }

    protected void loadFloat(int location, float value) {
        GL20.glUniform1f(location, value);
    }

    protected void loadInteger(int location, int value) {
        GL20.glUniform1i(location, value);
    }

    protected void loadVector(int location, Vector2f vector) {
        GL20.glUniform2f(location, vector.x, vector.y);
    }

    protected void loadVector(int location, Vector3f vector) {
        GL20.glUniform3f(location, vector.x, vector.y, vector.z);
    }

    protected void loadVector(int location, Vector4f vector) {
        GL20.glUniform4f(location, vector.x, vector.y, vector.z, vector.w);
    }

    protected void loadBoolean(int location, boolean value) {
        GL20.glUniform1f(location, value ? 1.0F : 0.0F);
    }

    protected void loadMatrix(int location, Matrix4f matrix) {
        buffer.put(matrix.m00());
        buffer.put(matrix.m01());
        buffer.put(matrix.m02());
        buffer.put(matrix.m03());
        buffer.put(matrix.m10());
        buffer.put(matrix.m11());
        buffer.put(matrix.m12());
        buffer.put(matrix.m13());
        buffer.put(matrix.m20());
        buffer.put(matrix.m21());
        buffer.put(matrix.m22());
        buffer.put(matrix.m23());
        buffer.put(matrix.m30());
        buffer.put(matrix.m31());
        buffer.put(matrix.m32());
        buffer.put(matrix.m33());

        buffer.flip();
        GL20.glUniformMatrix4fv(location, false, buffer);
    }

    public void start() {
        GL20.glUseProgram(programID);
    }

    public void stop() {
        GL20.glUseProgram(0);
    }

    public void clean() {
        stop();
        GL20.glDetachShader(programID, vertexShaderID);
        GL20.glDetachShader(programID, fragmentShaderID);
        GL20.glDeleteShader(vertexShaderID);
        GL20.glDeleteShader(fragmentShaderID);
        GL20.glDeleteProgram(programID);
    }

    private static int loadShader(String file, int type){
        StringBuilder shaderSource = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while((line = reader.readLine()) != null){
                shaderSource.append(line).append("//\n");
            }
            reader.close();
        } catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
        int shaderID = GL20.glCreateShader(type);
        GL20.glShaderSource(shaderID, shaderSource);
        GL20.glCompileShader(shaderID);
        if(GL20.glGetShaderi(shaderID, GL20.GL_COMPILE_STATUS) == GL11.GL_FALSE){
            System.out.println(GL20.glGetShaderInfoLog(shaderID, 500));
            System.err.println("Could not compile shader!");
            System.exit(-1);
        }
        return shaderID;
    }
}
