#version 330 core

in vec2 textureCoords;

out vec4 out_Color;

uniform sampler2D guiTexture;
uniform vec4 size;
uniform float overlay;
uniform vec4 overlayColor;

void main(void){

    vec4 textureColor = texture(guiTexture, textureCoords);

	if(!(textureCoords.x < size.x || textureCoords.y < size.y || textureCoords.x > size.z || textureCoords.y > size.w))
	        out_Color = mix(textureColor, vec4(1.0F, 1.0F, 1.0F, 1.0F), 0.5F * overlay) - vec4(1.0F - overlayColor.r, 1.0F - overlayColor.g, 1.0F - overlayColor.b, overlayColor.a);
	else
	    out_Color = vec4(0.0F, 0.0F, 0.0F, 0.0F);


}