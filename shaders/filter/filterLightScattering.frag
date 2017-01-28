#version 400 core

in vec2 textureCoords;
out vec4 out_Color;

uniform sampler2D colorBuffer;

const float decay = 0.96875;

void main()
{
    int samples = 128;
    float intensity = 0.250;
    vec2 textureCoords_local = textureCoords;

    vec2 direction = vec2(0.5) - textureCoords_local;
    direction /= samples;
    vec3 color = texture2D(colorBuffer, textureCoords_local).rgb;

    if(color.r < 0.8 || color.g < 0.8 || color.b < 0.8)
        intensity = 0.01;

    for(int samp = 0; samp < samples; samp++)
    {
        color += texture2D(colorBuffer, textureCoords_local).rgb * intensity;
        intensity *= decay;
        textureCoords_local += direction;
    }

    
    out_Color = vec4(color, 1.0);
}