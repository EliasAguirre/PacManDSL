#version 330

// Input attributes
layout (location = 0) in vec3 in_position;
layout (location = 1) in vec2 in_uv;

// Passed to fragment shader
out vec2 texture_coords;
out vec3 out_color;

// Application data
uniform mat3 transform;
uniform mat3 projection;
uniform vec3 color;

void main()
{
    out_color = color;
    texture_coords = in_uv;
    vec3 pos = projection * transform * vec3(in_position.xy, 1.0);
    gl_Position = vec4(pos.xy, in_position.z, 1.0);
}