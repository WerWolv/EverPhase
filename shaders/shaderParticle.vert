#version 330 core

in vec2 position;
in mat4 modelViewMatrix;
in vec4 texOffsets;
in float blendFactor;

out vec2 pass_textureCoords1;
out vec2 pass_textureCoords2;
out float blend;

uniform mat4 projectionMatrix;
uniform float numOfRows;

void main(void){

    vec2 pass_textureCoords = position + vec2(0.5, 0.5);
    pass_textureCoords.y = 1.0 - pass_textureCoords.y;
    pass_textureCoords /= numOfRows;
    pass_textureCoords1 = pass_textureCoords + texOffsets.xy;
    pass_textureCoords2 = pass_textureCoords + texOffsets.zw;
    blend = blendFactor;

	gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 0.0, 1.0);

}