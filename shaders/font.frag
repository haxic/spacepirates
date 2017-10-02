#version 400

	in vec2 pass_textureCoordinate;
	
	out vec4 result;
	
	uniform vec3 color;
	uniform sampler2D fontAtlas;
	
	// Higher width and lower edge values for high scale
	// Lower width and higher edge values for small scale
	const float width = 0.5;
	const float edge = 0.1;

	const float outlineWidth = 0.7;
	const float outlineEdge = 0.1;

	const vec3 outlineColor = vec3(1.0, 0.0, 0.0);
	
void main(void){
	float distance = 1.0 - texture(fontAtlas, pass_textureCoordinate).a;
	float alpha = 1.0 - smoothstep(width, width + edge, distance);
	
	float outlineDistance = 1.0 - texture(fontAtlas, pass_textureCoordinate).a;
	float outlineAlpha = 1.0 - smoothstep(outlineWidth, outlineWidth + outlineEdge, outlineDistance);
	
	float overallAlpha = alpha + (1.0 - alpha) * outlineAlpha;
	vec3 overallColor = mix(outlineColor, color, alpha / overallAlpha);
	
	result = vec4(overallColor, overallAlpha);
}