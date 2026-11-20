#ifdef GL_ES
precision mediump float;
#endif
varying vec4 v_color;
varying vec2 v_texCoords;
uniform sampler2D u_texture;
uniform float u_progress;
uniform float u_height;

void main() {
    vec4 base = texture2D(u_texture, v_texCoords) * v_color;
    float dist = abs(gl_FragCoord.y / u_height - 0.5) * 2.0;
    float frontier = u_progress * 1.12;
    float gray = 1.0 - smoothstep(frontier - 0.12, frontier, dist);
    float luminance = dot(base.rgb, vec3(0.2126, 0.7152, 0.0722));
    gl_FragColor = vec4(mix(base.rgb, vec3(luminance), gray), base.a);
}
