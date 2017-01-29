#version 140

in vec2 pass_textureCoords1;
in vec2 pass_textureCoords2;
in float blend;

out vec4 out_color;

uniform sampler2D particleTexture;

void main(void){

    vec4 color1 = texture(particleTexture, pass_textureCoords1);
    vec4 color2 = texture(particleTexture, pass_textureCoords2);

	out_color = mix(color1, color2, blend);

}