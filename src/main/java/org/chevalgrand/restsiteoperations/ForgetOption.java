package org.chevalgrand.restsiteoperations;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.rooms.CampfireUI;
import com.megacrit.cardcrawl.rooms.RestRoom;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import com.megacrit.cardcrawl.vfx.cardManip.PurgeCardEffect;
import org.chevalgrand.keywords.RaceKeywords;
import org.chevalgrand.mechanics.DeckSelection;
import org.chevalgrand.mechanics.Stamina;

public final class ForgetOption extends AbstractCampfireOption {
    private static final int LIMIT = 4;

    public ForgetOption() {
        String[] text = RaceKeywords.text("Forget");
        usable = !candidates().isEmpty();
        label = text[0];
        description = usable ? text[1] : text[2];
        img = ImageMaster.CAMPFIRE_TOKE_BUTTON;
    }

    private static CardGroup candidates() {
        CardGroup group = CardGroup.getGroupWithoutBottledCards(AbstractDungeon.player.masterDeck.getPurgeableCards());
        group.group.removeIf(card -> !Stamina.has(card, 0));
        return group;
    }

    @Override
    public void useOption() {
        RestRoom room = (RestRoom) AbstractDungeon.getCurrRoom();
        CardGroup choices = candidates();
        if (choices.isEmpty()) {
            room.campfireUI.reopen();
            return;
        }
        CampfireUI.hidden = true;
        DeckSelection.open(choices, LIMIT, RaceKeywords.text("Forget")[3], selected -> {
            if (selected.isEmpty()) {
                room.campfireUI.reopen();
                return;
            }
            CardCrawlGame.sound.play("CARD_EXHAUST");
            for (AbstractCard card : selected) {
                AbstractDungeon.player.masterDeck.removeCard(card);
                CardCrawlGame.metricData.addCampfireChoiceData("CGS_FORGET", card.getMetricID());
                AbstractDungeon.topLevelEffects.add(new PurgeCardEffect(card, card.current_x, card.current_y));
            }
            room.phase = AbstractRoom.RoomPhase.COMPLETE;
            room.cutFireSound();
            AbstractDungeon.overlayMenu.proceedButton.show();
        });
    }
}
