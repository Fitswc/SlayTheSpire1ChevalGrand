package org.chevalgrand.visuals;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.chevalgrand.powers.DomainPower;

public final class DomainVisuals {
    private static final float FADE_SECONDS = 1.8f;
    private static float elapsed;
    private static AbstractRoom activeRoom;
    private static ShaderProgram grayscale;
    private static boolean shaderLoaded;

    private DomainVisuals() {}

    public static void renderBackground(AbstractScene scene, SpriteBatch batch) {
        AbstractPlayer player = AbstractDungeon.player;
        if (!active(player)) {
            activeRoom = null;
            scene.renderCombatRoomBg(batch);
            return;
        }
        AbstractRoom room = AbstractDungeon.getCurrRoom();
        if (activeRoom != room) {
            activeRoom = room;
            elapsed = 0f;
        }
        elapsed = Math.min(FADE_SECONDS, elapsed + Gdx.graphics.getDeltaTime());
        if (!shaderLoaded) {
            grayscale = ScreenShaders.load("domain-background.frag");
            shaderLoaded = true;
        }
        if (grayscale == null) {
            scene.renderCombatRoomBg(batch);
            return;
        }
        float remaining = 1f - elapsed / FADE_SECONDS;
        ShaderProgram previous = batch.getShader();
        batch.setShader(grayscale);
        try {
            grayscale.setUniformf("u_progress", 1f - remaining * remaining * remaining);
            grayscale.setUniformf("u_height", Gdx.graphics.getBackBufferHeight());
            scene.renderCombatRoomBg(batch);
        } finally {
            batch.setShader(previous);
        }
    }

    public static void renderAura(AbstractPlayer player, SpriteBatch batch) {
        if (!active(player) || player.img == null) return;
        Texture body = player.img;
        float width = body.getWidth() * Settings.scale;
        float height = body.getHeight() * Settings.scale;
        float x = player.drawX + player.animX - width / 2f;
        float y = player.drawY;
        float alpha = Math.min(1f, elapsed / 0.3f);
        Color previous = batch.getColor().cpy();
        for (int i = 3; i >= 1; i--) {
            float offset = i * 5f * Settings.scale;
            batch.setColor(0.14f, 0.91f, 0.88f, alpha * (0.32f - i * 0.06f));
            batch.draw(body, x - offset, y + offset * 0.12f, width, height,
                    0, 0, body.getWidth(), body.getHeight(), player.flipHorizontal, false);
        }
        batch.setColor(previous);
    }

    private static boolean active(AbstractPlayer player) {
        return !Settings.DISABLE_EFFECTS && player != null && !player.isDead && player.hasPower(DomainPower.POWER_ID)
                && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !AbstractDungeon.getCurrRoom().isBattleOver;
    }
}
