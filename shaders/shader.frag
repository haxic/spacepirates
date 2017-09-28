#version 400 core

in vec2 pass_textureCoordinate;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 toCameraVector;

out vec4 result;

uniform sampler2D textureSampler;
uniform vec3 lightColor;
uniform float shineDamper;
uniform float reflectivity;
uniform float ambientLight;
uniform float allowBackLighting;

void main(void) {
	vec4 textureColor = texture(textureSampler, pass_textureCoordinate);
	// Discard transparent pixels.
	if (textureColor.a < 0.5) {
		discard;
	}
	vec3 actualSurfaceNormal = surfaceNormal;
	if (allowBackLighting > 0.5 && dot(actualSurfaceNormal, toCameraVector) < 0.0) {
		actualSurfaceNormal = -actualSurfaceNormal;
	}

	// Per-pixel lighting
	vec3 unitNormal = normalize(actualSurfaceNormal);
	vec3 unitToLightVector = normalize(toLightVector);
	float brightness = max(dot(unitNormal, unitToLightVector), ambientLight);
	vec3 diffuse = brightness * lightColor;
	
	// Specular lighting
	vec3 unitToCameraVector = normalize(toCameraVector);
	vec3 lightDirection = -unitToLightVector;
	vec3 reflectedLightDirection = reflect(lightDirection, unitNormal);
	float reflectionFactor = max(dot(reflectedLightDirection, unitToCameraVector), 0.0);
	float dampenFactor = pow(reflectionFactor, shineDamper);
	vec3 specularFactor = dampenFactor * reflectivity * lightColor;
		
	result = vec4(diffuse, 1.0) * textureColor + vec4(specularFactor, 1.0);
}