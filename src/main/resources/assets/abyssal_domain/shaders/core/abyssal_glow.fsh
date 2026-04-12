#version 150

uniform sampler2D DiffuseSampler;

in vec4 vertexColor;
in vec2 texCoord0;

out vec4 fragColor;

void main() {
    vec4 color = texture(DiffuseSampler, texCoord0);
    float brightness = (color.r + color.g + color.b) / 3.0;
    vec3 glow = color.rgb * brightness * 1.5;
    fragColor = vec4(mix(color.rgb, glow, 0.3), color.a);
}
