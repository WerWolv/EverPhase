package com.werwolv.shader;

public class ShaderStatic extends Shader {

    public ShaderStatic() {
        super("vertexShader", "fragmentShader");
    }

    @Override
    protected void bindAttributes() {
        super.bindAttribute(0, "position");
        super.bindAttribute(1, "textureCoords");
    }
}
