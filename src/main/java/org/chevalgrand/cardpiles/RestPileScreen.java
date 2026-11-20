package org.chevalgrand.cardpiles;

import basemod.abstracts.CustomScreen;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import org.chevalgrand.cards.RaceCard;
import org.chevalgrand.keywords.RaceKeywords;
import org.chevalgrand.mechanics.Stamina;

public final class RestPileScreen extends CustomScreen {
    @SpireEnum
    public static AbstractDungeon.CurrentScreen CHEVAL_REST_PILE;

    private static final int COLUMNS = 5;
    private CardGroup cards;
    private float scroll;

    @Override
    public AbstractDungeon.CurrentScreen curScreen() {
        return CHEVAL_REST_PILE;
    }

    @Override
    public void open(Object... args) {
        cards = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        cards.group.addAll(Stamina.restingCards());
        scroll = 0f;
        positionCards(true);
        reopen();
    }

    @Override
    public void reopen() {
        AbstractDungeon.screen = CHEVAL_REST_PILE;
        AbstractDungeon.isScreenUp = true;
        AbstractDungeon.overlayMenu.showBlackScreen();
        AbstractDungeon.overlayMenu.cancelButton.show(RaceKeywords.text("RestPileScreen")[1]);
    }

    @Override
    public void close() {
        if (cards != null) {
            cards.group.forEach(card -> {
                card.unhover();
                card.untip();
            });
            cards = null;
        }
        AbstractDungeon.overlayMenu.cancelButton.hide();
        genericScreenOverlayReset();
    }

    @Override
    public void update() {
        if (cards == null || CardCrawlGame.isPopupOpen) return;
        if (InputHelper.pressedEscape) {
            InputHelper.pressedEscape = false;
            AbstractDungeon.closeCurrentScreen();
            return;
        }
        int rows = (cards.size() + COLUMNS - 1) / COLUMNS;
        float limit = Math.max(0, (rows - 1) * 285f * Settings.scale - Settings.HEIGHT * 0.48f);
        if (InputHelper.scrolledDown) scroll = Math.min(limit, scroll + 90f * Settings.scale);
        if (InputHelper.scrolledUp) scroll = Math.max(0, scroll - 90f * Settings.scale);
        positionCards(false);
        for (com.megacrit.cardcrawl.cards.AbstractCard card : cards.group) {
            card.update();
            if (!inView(card.current_y)) {
                card.unhover();
                continue;
            }
            card.updateHoverLogic();
            if (card.hb.hovered && InputHelper.justReleasedClickLeft) {
                InputHelper.justReleasedClickLeft = false;
                CardCrawlGame.cardPopup.open(card, cards);
                break;
            }
        }
    }

    private void positionCards(boolean immediate) {
        for (int i = 0; i < cards.size(); i++) {
            RaceCard card = (RaceCard) cards.group.get(i);
            card.target_x = Settings.WIDTH / 2f + (i % COLUMNS - 2) * 260f * Settings.scale;
            card.target_y = Settings.HEIGHT * 0.70f + scroll - (i / COLUMNS) * 285f * Settings.scale;
            card.targetDrawScale = 0.68f;
            if (immediate) {
                card.current_x = card.target_x;
                card.current_y = card.target_y;
                card.drawScale = card.targetDrawScale;
            }
        }
    }

    private boolean inView(float y) {
        return y >= Settings.HEIGHT * 0.11f && y <= Settings.HEIGHT * 0.75f;
    }

    @Override
    public void render(SpriteBatch batch) {
        if (cards == null) return;
        String title = RaceKeywords.text(RaceKeywords.REST_PILE)[0];
        for (com.megacrit.cardcrawl.cards.AbstractCard card : cards.group) {
            if (inView(card.current_y)) card.render(batch);
        }
        FontHelper.renderFontCentered(batch, FontHelper.panelNameFont, title, Settings.WIDTH / 2f,
                Settings.HEIGHT * 0.90f, Settings.GOLD_COLOR);
        if (cards.isEmpty()) {
            FontHelper.renderFontCentered(batch, FontHelper.panelNameFont, RaceKeywords.text("RestPileScreen")[0],
                    Settings.WIDTH / 2f, Settings.HEIGHT * 0.5f, Settings.CREAM_COLOR);
        }
    }

    @Override
    public void openingSettings() {
        AbstractDungeon.previousScreen = CHEVAL_REST_PILE;
    }
}
