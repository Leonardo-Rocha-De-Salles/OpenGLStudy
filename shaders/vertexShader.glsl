#version 330 core
layout (location = 0) in vec3 aPos; //attribute position 0
layout (location = 1) in vec3 aColor; //attribute position 1
//out vec3 aColor;
out vec3 acPos;

uniform float offSet;

void main() {
    gl_Position = vec4(aPos.x - offSet, aPos.y, aPos.z, 1.0);
    acPos = aPos;
    //ourColor = aColor;
}