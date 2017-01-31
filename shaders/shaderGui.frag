#version 400 core

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec4 size;
uniform float overlay;

void main(void){

	if(!(textureCoords.x < size.x || textureCoords.y < size.y || textureCoords.x > size.z || textureCoords.y > size.w)) {
	    out_Color = mix(texture(guiTexture,textureCoords), vec4(1.0F, 1.0F, 1.0F, 1.0F), 0.5F * overlay);
	} else {
	    out_Color = vec4(0.0F, 0.0F, 0.0F, 0.0F);
	}

}