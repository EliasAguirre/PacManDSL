#version 330

in vec2 texture_coords;
in vec3 out_color;
// Application data
uniform sampler2D sampler0;

// Output color
layout(location = 0) out  vec4 color;

void main() {
    color = texture(sampler0, texture_coords) * vec4(out_color, 1.0);
}
