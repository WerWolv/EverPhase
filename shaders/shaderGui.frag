#version 400 core

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec4 size;

void main(void){

	if((textureCoords.x < size.x || textureCoords.y < size.y || textureCoords.x > size.z || textureCoords.y > size.w)) discard;
	    out_Color = texture(guiTexture,textureCoords);

}