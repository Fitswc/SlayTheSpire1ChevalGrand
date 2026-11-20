package org.chevalgrand.visuals;

import basemod.interfaces.PostRenderSubscriber;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.chevalgrand.characters.ChevalGrandEnums;

public final class LowHealthScreen implements PostRenderSubscriber {
    private ShaderProgram shader;
    private boolean shaderLoaded;
    private TextureRegion captured;
    private float time;

    @Override
    public void receivePostRender(SpriteBatch batch) {
        if (!shouldRender()) {
            time = 0f;
            if (captured != null) {
                captured.getTexture().dispose();
                captured = null;
            }
            return;
        }
        if (!shaderLoaded) {
            shader = ScreenShaders.load("low-health.frag");
            shaderLoaded = true;
        }
        if (shader == null) return;
        int width = Gdx.graphics.getBackBufferWidth();
        int height = Gdx.graphics.getBackBufferHeight();
        if (width <= 0 || height <= 0) return;
        if (captured == null || captured.getRegionWidth() != width || captured.getRegionHeight() != height) {
            if (captured != null) captured.getTexture().dispose();
            captured = new TextureRegion(new Texture(width, height, Pixmap.Format.RGBA8888));
            captured.flip(false, true);
        }

        time = (time + Gdx.graphics.getDeltaTime()) % 9f;
        batch.flush();
        captured.getTexture().bind(0);
        Gdx.gl.glCopyTexSubImage2D(GL20.GL_TEXTURE_2D, 0, 0, 0, 0, 0, width, height);
        ShaderProgram previousShader = batch.getShader();
        Color previousColor = batch.getColor().cpy();
        batch.setShader(shader);
        try {
            shader.setUniformf("u_inverseWidth", 1f / width);
            shader.setUniformf("u_time", time);
            batch.setColor(Color.WHITE);
            batch.draw(captured, 0, 0, Settings.WIDTH, Settings.HEIGHT);
        } finally {
            batch.setShader(previousShader);
            batch.setColor(previousColor);
        }
    }

    private boolean shouldRender() {
        AbstractPlayer player = AbstractDungeon.player;
        return !Settings.DISABLE_EFFECTS && CardCrawlGame.mode == CardCrawlGame.GameMode.GAMEPLAY
                && AbstractDungeon.currMapNode != null && player != null && player.chosenClass == ChevalGrandEnums.PLAYER_CLASS
                && !player.isDead && player.currentHealth > 0 && player.currentHealth <= player.maxHealth * 0.30f;
    }
}
