#version 330 core

in vec3 position;
in vec2 textureCoords;
in vec3 normal;

out vec2 pass_textureCoords;
out vec3 surfaceNormal;
out vec3 toLightVector[16];
out vec3 toCameraVector;
out float visibility;

uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPos[16];

uniform float useFakeLightning;

uniform float numOfRows;
uniform vec2 offset;

const float density = 0.0035;
const float gradient = 5.0;

uniform vec4 plane;

void main(void) {

    vec4 worldPos = transformationMatrix * vec4(position, 1.0);

    gl_ClipDistance[0] = dot(worldPos, plane);

    vec4 posRelToCam = viewMatrix * worldPos;

    gl_Position = projectionMatrix * posRelToCam;
    pass_textureCoords = (textureCoords / numOfRows) + offset;

    vec3 actualNormal = normal;

    if(useFakeLightning > 0.5)
        actualNormal = vec3(0.0, 1.0, 0.0);


    surfaceNormal = (transformationMatrix * vec4(normal, 0.0)).xyz;

    for(int i = 0; i < 16; i++)
        toLightVector[i] = lightPos[i] - worldPos.xyz;

    toCameraVector = (inverse(viewMatrix) * vec4(0.0, 0.0, 0.0, 1.0)).xyz;

    float distance = length(posRelToCam.xyz);
    visibility = exp(-pow((distance * density), gradient));
    visibility = clamp(visibility, 0.0, 1.0);

}