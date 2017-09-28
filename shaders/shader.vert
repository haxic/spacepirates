#version 400 core

in vec3 position;
in vec2 textureCoordinate;
in vec3 normal;

out vec2 pass_textureCoordinate;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 toCameraVector;

uniform mat4 projection;
uniform mat4 view;
uniform mat4 model;
uniform vec3 lightPosition;


void main(void) {
	// Position on screen
	vec4 worldPosition = model * vec4(position, 1.0);
	gl_Position = projection * view * worldPosition;
	pass_textureCoordinate = textureCoordinate;
		
	// Specular lighting
	toCameraVector = (inverse(view) * vec4(0.0, 0.0, 0.0, 1.0)).xyz - worldPosition.xyz;
	
	// Lighting
	surfaceNormal = (model * vec4(normal, 0.0)).xyz;
	toLightVector = lightPosition - worldPosition.xyz;
}
