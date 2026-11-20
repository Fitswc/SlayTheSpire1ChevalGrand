#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_inverseWidth;
uniform float u_time;

void main() {
    vec2 offset = vec2(u_inverseWidth, 0.0);
    vec3 base = vec3(texture2D(u_texture, v_texCoords + offset).r,
                     texture2D(u_texture, v_texCoords).g,
                     texture2D(u_texture, v_texCoords - offset).b);
    float luminance = dot(base, vec3(0.2126, 0.7152, 0.0722));
    vec3 color = mix(vec3(luminance), base, 0.60);
    float scan = u_time / 5.0 - 0.15;
    float band = 1.0 - smoothstep(0.01, 0.14, abs(v_texCoords.x - scan));
    float stripe = 1.0 - smoothstep(0.04, 0.22, abs(fract(v_texCoords.x * 90.0) - 0.5));
    gl_FragColor = vec4(color * (1.0 - 0.12 * band * stripe), 1.0) * v_color;
}
