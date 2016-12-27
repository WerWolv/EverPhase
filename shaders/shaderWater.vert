#version 400 core

in vec2 position;

out vec4 clipSpace;
out vec2 textureCoords;
out vec3 toCameraVector;
out vec3 fromLightVector[16];

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform vec3 cameraPos;
uniform vec3 lightPos[16];

const float tiling = 4.0;

void main(void) {

    vec4 worldPos = modelMatrix * vec4(position.x, 0.0, position.y, 1.0);

    clipSpace = projectionMatrix * viewMatrix * worldPos;
	gl_Position = clipSpace;
    textureCoords = vec2(position.x / 2.0 + 0.5, position.y / 2.0 + 0.5) * tiling;
    toCameraVector = cameraPos - worldPos.xyz;

    for(int i = 0; i < 16; i++)
        fromLightVector[i] = worldPos.xyz - lightPos[i];
}