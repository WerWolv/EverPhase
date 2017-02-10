#version 330 core

in vec2 pass_textureCoords;
in vec3 toLightVector[4];
in vec3 toCameraVector;
in float visibility;

out vec4 out_Color;

uniform sampler2D modelSampler;
uniform sampler2D normalSampler;
uniform sampler2D extraInfoMapSampler;

uniform float usesExtraInfoMap;
uniform vec3 lightColor[4];
uniform vec3 attenuation[4];
uniform float shineDamper;
uniform float reflectivity;
uniform vec3 skyColor;

void main(void){

    vec4 normalMapValue = 2.0 * texture(normalSampler, pass_textureCoords) - 1.0;

	vec3 unitNormal = normalize(normalMapValue.rgb);
	vec3 unitVectorToCamera = normalize(toCameraVector);
	
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	
	for(int i=0;i<4;i++){
		float distance = length(toLightVector[i]);
		float attFactor = attenuation[i].x + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitLightVector = normalize(toLightVector[i]);	
		float nDotl = dot(unitNormal,unitLightVector);
		float brightness = max(nDotl,0.0);
		vec3 lightDirection = -unitLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection,unitNormal);
		float specularFactor = dot(reflectedLightDirection , unitVectorToCamera);
		specularFactor = max(specularFactor,0.0);
		float dampedFactor = pow(specularFactor,shineDamper);
		totalDiffuse = totalDiffuse + (brightness * lightColor[i])/attFactor;
		totalSpecular = totalSpecular + (dampedFactor * reflectivity * lightColor[i])/attFactor;
	}
	totalDiffuse = max(totalDiffuse, 0.2);
	
	vec4 textureColor = texture(modelSampler,pass_textureCoords);
	if(textureColor.a<0.5){
		discard;
	}

    if(usesExtraInfoMap > 0.5) {
        vec4 specularMapInfo = texture(extraInfoMapSampler, pass_textureCoords);
        totalSpecular *= specularMapInfo.r;

        if(specularMapInfo.g > 0.5)
            totalDiffuse = vec3(1.0, 1.0, 1.0);
    }

	out_Color =  vec4(totalDiffuse,1.0) * textureColor + vec4(totalSpecular,1.0);
	out_Color = mix(vec4(skyColor,1.0),out_Color, visibility);
}