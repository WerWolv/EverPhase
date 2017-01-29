#version 400 core

in vec2 position;

out vec2 pass_textureCoords1;
out vec2 pass_textureCoords2;
out float blend;

uniform mat4 projectionMatrix;
uniform mat4 modelViewMatrix;

uniform vec2 texOffset1;
uniform vec2 texOffset2;
uniform vec2 texCoordInfo;

void main(void){

    vec2 pass_textureCoords = position + vec2(0.5, 0.5);
    pass_textureCoords.y = 1.0 - pass_textureCoords.y;
    pass_textureCoords /= texCoordInfo.x;
    pass_textureCoords1 = pass_textureCoords + texOffset1;
    pass_textureCoords2 = pass_textureCoords + texOffset2;
    blend = texCoordInfo.y;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}