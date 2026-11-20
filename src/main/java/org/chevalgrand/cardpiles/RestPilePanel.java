package org.chevalgrand.cardpiles;

import basemod.BaseMod;
import basemod.interfaces.PostDungeonUpdateSubscriber;
import basemod.interfaces.PostRenderSubscriber;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.characters.ChevalGrandEnums;
import org.chevalgrand.keywords.RaceKeywords;
import org.chevalgrand.mechanics.Stamina;

public final class RestPilePanel implements PostDungeonUpdateSubscriber, PostRenderSubscriber {
    private static final float PANEL_X = 222f;
    private static final float PANEL_Y = 63f;

    private final Texture icon = ImageMaster.loadImage(ChevalGrandSlayMod.IMAGES + "cardpiles/rest.png");
    private final Hitbox button = new Hitbox(80f * Settings.scale, 80f * Settings.scale);

    public static void register() {
        BaseMod.addCustomScreen(new RestPileScreen());
        BaseMod.subscribe(new RestPilePanel());
    }

    private boolean visible() {
        return CardCrawlGame.mode == CardCrawlGame.GameMode.GAMEPLAY && AbstractDungeon.player != null
                && AbstractDungeon.player.chosenClass == ChevalGrandEnums.PLAYER_CLASS
                && AbstractDungeon.currMapNode != null && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT
                && !AbstractDungeon.getCurrRoom().isBattleOver && !Settings.hideLowerElements
                && (!AbstractDungeon.isScreenUp || AbstractDungeon.screen == RestPileScreen.CHEVAL_REST_PILE);
    }

    @Override
    public void receivePostDungeonUpdate() {
        if (!visible() || AbstractDungeon.isScreenUp || AbstractDungeon.player.isDraggingCard) {
            button.unhover();
            return;
        }
        button.move(PANEL_X * Settings.scale, PANEL_Y * Settings.scale);
        button.update();
        if (button.justHovered) CardCrawlGame.sound.playA("UI_HOVER", -0.4f);
        if (button.hovered && InputHelper.justClickedLeft) {
            InputHelper.justClickedLeft = false;
            BaseMod.openCustomScreen(RestPileScreen.CHEVAL_REST_PILE);
        }
    }

    @Override
    public void receivePostRender(SpriteBatch batch) {
        if (!visible() || icon == null) return;
        float scale = Settings.scale;
        float x = PANEL_X * scale;
        float y = PANEL_Y * scale;
        float size = (button.hovered ? 73f : 67f) * scale;
        float countX = x + 20f * scale;
        float countY = y - 20f * scale;
        // The vanilla badge texture has transparent margins around the circle.
        float badgeSize = 94f * scale;
        Color previous = batch.getColor().cpy();
        batch.setColor(Color.WHITE);
        batch.draw(icon, x - size / 2f, y - size / 2f, size, size);
        batch.draw(ImageMaster.DECK_COUNT_CIRCLE, countX - badgeSize / 2f, countY - badgeSize / 2f, badgeSize, badgeSize);
        FontHelper.renderFontCentered(batch, FontHelper.turnNumFont, Integer.toString(Stamina.restingCards().size()),
                countX, countY, Settings.CREAM_COLOR, 0.75f);
        batch.setColor(previous);
        if (button.hovered) {
            String[] text = RaceKeywords.text(RaceKeywords.REST_PILE);
            TipHelper.renderGenericTip(x + 42f * Settings.scale, y + 104f * Settings.scale, text[0], text[1]);
        }
    }
}
