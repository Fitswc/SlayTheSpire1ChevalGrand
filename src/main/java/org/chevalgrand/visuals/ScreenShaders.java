package org.chevalgrand.visuals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import org.apache.logging.log4j.LogManager;

final class ScreenShaders {
    private static final String PATH = "ChevalGrandSlay/shaders/";

    private ScreenShaders() {}

    static ShaderProgram load(String fragment) {
        ShaderProgram shader = new ShaderProgram(Gdx.files.internal(PATH + "screen.vert"), Gdx.files.internal(PATH + fragment));
        if (shader.isCompiled()) return shader;
        LogManager.getLogger(ScreenShaders.class).warn("Could not compile {}: {}", fragment, shader.getLog());
        shader.dispose();
        return null;
    }
}
