#version 330 core

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[16];
in vec3 toCameraVector;
in float visibility;

out vec4 out_color;

uniform sampler2D textureSampler;
uniform sampler2D extraInfoMapSampler;

uniform float usesExtraInfoMap;
uniform vec3 lightColor[16];
uniform vec3 attenuation[16];

uniform float shineDamper;
uniform float reflectivity;

uniform vec3 skyColor;

void main(void) {

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitVectorToCamera = normalize(toCameraVector);

    vec3 totalDiffuse = vec3(0.0);
    vec3 totalSpecular = vec3(0.0);

    for(int i = 0; i < 16; i++) {
        float distance = length(toLightVector[i]);
        float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);

        vec3 unitLightVector = normalize(toLightVector[i]);
        float nDotl = dot(unitNormal, unitLightVector);
        float brightness = max(nDotl, 0.0);

        vec3 lightDirection = -unitVectorToCamera;
        vec3 reflectLightDirection = reflect(lightDirection, unitNormal);

        float specularFactor = dot(reflectLightDirection, unitVectorToCamera);
        specularFactor = max(specularFactor, 0.0);
        float dampedFactor = pow(specularFactor, shineDamper);

        totalDiffuse += (brightness * lightColor[i]) / attFactor;
        totalSpecular += (dampedFactor * reflectivity * lightColor[i]) / attFactor;
    }

    totalDiffuse = max(totalDiffuse, 0.15);

    vec4 textureColor = texture(textureSampler, pass_textureCoords);

    if(textureColor.a < 0.5)
        discard;

    if(usesExtraInfoMap > 0.5) {
        vec4 specularMapInfo = texture(extraInfoMapSampler, pass_textureCoords);
        totalSpecular *= specularMapInfo.r;

        if(specularMapInfo.g > 0.5)
            totalDiffuse = vec3(1.0, 1.0, 1.0);
    }

    out_color = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
    out_color = mix(vec4(skyColor, 1.0), out_color, visibility);
}