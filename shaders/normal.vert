#version 400 core

in vec3 position;
in vec2 textureCoordinate;
in vec3 normal;
in vec3 tangent;

out vec2 pass_textureCoordinate;
out vec3 toLightVector[4];
out vec3 toCameraVector;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;

uniform vec3 lightPosition[64];
uniform int numberOfLights;

uniform float atlasSize;
uniform vec2 textureOffset;


void main(void) {
	
	// Position on screen
	vec4 worldPosition = model * vec4(position, 1.0);
	mat4 modelViewMatrix = view * model;
	vec4 positionRelativeToCam = modelViewMatrix * vec4(position, 1.0);
	gl_Position = projection * positionRelativeToCam;
	pass_textureCoordinate = (textureCoordinate/atlasSize) + textureOffset;
		
	vec3 surfaceNormal = (model * vec4(normal, 0.0)).xyz;
	
	vec3 norm = normalize(surfaceNormal);
	vec3 tang = normalize((modelViewMatrix * vec4(tangent, 0.0)).xyz);
	vec3 bitang = normalize(cross(norm, tang));
	mat3 toTangentSpace = mat3(
		tang.x, bitang.x, norm.x,
		tang.y, bitang.y, norm.y,
		tang.z, bitang.z, norm.z
	);
	
	for (int i = 0; i < numberOfLights; i++) {
		toLightVector[i] = toTangentSpace * (lightPosition[i] - worldPosition.xyz);
		
	}
	//toCameraVector = toTangentSpace * ((inverse(view) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz);
	toCameraVector = toTangentSpace * (-positionRelativeToCam.xyz);
}
