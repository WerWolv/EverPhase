#version 330 core

in vec2 textureCoords;

out vec4 out_Color;

uniform vec2 resolution;
uniform sampler2D vignetteTex;

const float outerRadius = 0.65, innerRadius = 0.4, intensity = 0.2;

void main(void){
    vec4 color = texture2D(vignetteTex, textureCoords) * vec4(0.9, 0.9, 0.9, 1);

    vec2 relativePos = gl_FragCoord.xy / resolution - 0.5;
    float length = length(relativePos);
    float vignette = smoothstep(outerRadius, innerRadius, length);

    color.rgb = mix(color.rgb, color.rgb * vignette, intensity);

    out_Color = color;

}