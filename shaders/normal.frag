#version 400 core

in vec2 pass_textureCoordinate;
in vec3 pass_tangent;
in vec3 toLightVector[4];
in vec3 toCameraVector;

out vec4 result;

uniform sampler2D textureSampler;
uniform sampler2D normalMap;

// Light sources
uniform int numberOfLights;
uniform vec3 lightColor[64];
uniform vec3 attenuation[64];

// Material attributes
uniform float shineDamper;
uniform float reflectivity;

// Ambient lighting
uniform float ambientLight;

// Object options
uniform float allowBackLighting;

void main(void) {
	vec4 textureColor = texture(textureSampler, pass_textureCoordinate);
	// Discard transparent pixels.
	if (textureColor.a < 0.5) {
		discard;
	}
	
	vec4 normalMapValue = 2.0 * texture(normalMap, pass_textureCoordinate) - 1.0;
	
	
	
	vec3 unitNormal = normalize(normalMapValue.rgb);
	vec3 unitToCameraVector = normalize(toCameraVector);
	
	if (allowBackLighting > 0.5 && dot(unitNormal, toCameraVector) < 0.0) {
		unitNormal = -unitNormal;
	}
	
	// Lighting
	vec3 totalDiffuse = vec3(0.0);
	vec3 totalSpecular = vec3(0.0);
	for (int i = 0; i < numberOfLights; i++) {
		float distance = length(toLightVector[i]);
		float attenuationFactor = (attenuation[i].x) + (attenuation[i].y * distance) + (attenuation[i].z * distance * distance);
		vec3 unitToLightVector = normalize(toLightVector[i]);

		// Per-pixel lighting
		float brightness = max(dot(unitNormal, unitToLightVector), 0.0);
		totalDiffuse = totalDiffuse + (brightness * lightColor[i]) / attenuationFactor;
		
		// Specular lighting
		vec3 lightDirection = -unitToLightVector;
		vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
		float reflectionFactor = max(dot(reflectedLightDirection, unitToCameraVector), 0.0);
		float dampenFactor = pow(reflectionFactor, shineDamper);
		totalSpecular = totalSpecular + (dampenFactor * reflectivity * lightColor[i]) / attenuationFactor;
	}
	totalDiffuse = max(totalDiffuse, ambientLight);

	result = vec4(totalDiffuse, 1.0) * textureColor + vec4(totalSpecular, 1.0);
}