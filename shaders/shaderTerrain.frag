#version 150

in vec2 pass_textureCoords;
in vec3 surfaceNormal;
in vec3 toLightVector[16];
in vec3 toCameraVector;
in float visibility;
in vec4 shadowCoords;

out vec4 out_color;

uniform sampler2D backgroundTexture;
uniform sampler2D rTexture;
uniform sampler2D gTexture;
uniform sampler2D bTexture;
uniform sampler2D blendMap;
uniform sampler2D shadowMap;

uniform vec3 lightColor[16];
uniform vec3 attenuation[16];

uniform float shineDamper;
uniform float reflectivity;

uniform vec3 skyColor;

const int pcfCnt = 2;
const float totalTexels = (pcfCnt * 2.0 + 1.0) * (pcfCnt * 2.0 + 1.0);

void main(void) {

    ivec2 mapSize = textureSize(shadowMap, 0);
    float texelSize = 1.0 / mapSize.x;
    float totalShadowSamples = 0.0;

    for(int x = -pcfCnt; x <= pcfCnt; x++) {
        for(int y = -pcfCnt; y <= pcfCnt; y++) {
            float objectNearestLight = texture(shadowMap, shadowCoords.xy + vec2(x, y) * texelSize).r;

            if(shadowCoords.z > objectNearestLight + 0.002)
                totalShadowSamples++;
        }
    }

    totalShadowSamples /= totalTexels;

    float lightFactor = 1.0 - (totalShadowSamples * shadowCoords.w);

    vec4 blendMapColor = texture(blendMap, pass_textureCoords);
    float backTextureAmount = 1 - (blendMapColor.r + blendMapColor.g + blendMapColor.b);
    vec2 tiledCoords = pass_textureCoords * 40.0;
    vec4 backgroundTextureColor = texture(backgroundTexture, tiledCoords) * backTextureAmount;
    vec4 rTextureColor = texture(rTexture, tiledCoords) * blendMapColor.r;
    vec4 gTextureColor = texture(gTexture, tiledCoords) * blendMapColor.g;
    vec4 bTextureColor = texture(bTexture, tiledCoords) * blendMapColor.b;

    vec4 totalColor = backgroundTextureColor + rTextureColor + gTextureColor + bTextureColor;

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

    totalDiffuse = max(totalDiffuse * lightFactor, 0.4) * lightFactor;

    out_color = vec4(totalDiffuse, 1.0) * totalColor + vec4(totalSpecular, 1.0);
    out_color = mix(vec4(skyColor, 1.0), out_color, visibility);
}