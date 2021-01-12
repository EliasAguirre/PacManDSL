#version 330

in vec3 out_color;
// Output color
layout(location = 0) out vec3 color;

void main() {
    color = out_color;
}
