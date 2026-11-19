package org.chevalgrand.mechanics;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.chevalgrand.cards.RaceCard;

public final class CardSelection extends AbstractGameAction {
    public enum HandEffect { DISCOUNT, RETAIN, EXHAUST }

    private final Supplier<CardGroup> candidates;
    private final Consumer<AbstractCard> apply;
    private final int limit;
    private final boolean anyNumber;
    private boolean opened;

    public CardSelection(Supplier<CardGroup> candidates, Consumer<AbstractCard> apply, int limit, boolean anyNumber) {
        this.candidates = candidates;
        this.apply = apply;
        this.limit = limit;
        this.anyNumber = anyNumber;
    }

    public static CardSelection fromRest(int limit, boolean skillOnly, boolean anyNumber, boolean toHand) {
        return new CardSelection(() -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (RaceCard card : Stamina.restingCards()) {
                if (!skillOnly || card.type == AbstractCard.CardType.SKILL) group.addToBottom(card);
            }
            return group;
        }, card -> {
            RaceCard resting = (RaceCard) card;
            if (toHand) Stamina.restoreToHand(resting);
            else Stamina.moveToDraw(resting, resting.maxStamina());
        }, limit, anyNumber);
    }

    public static CardSelection fromHand(HandEffect effect, RaceCard source) {
        return new CardSelection(() -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card == source) continue;
                switch (effect) {
                    case DISCOUNT:
                        if (!(card instanceof RaceCard) || ((RaceCard) card).maxStamina() == 0 || card.cost == -1) continue;
                        break;
                    case EXHAUST:
                        if (card.type != AbstractCard.CardType.STATUS && card.type != AbstractCard.CardType.CURSE) continue;
                        break;
                    default:
                        break;
                }
                group.addToBottom(card);
            }
            return group;
        }, card -> {
            switch (effect) {
                case DISCOUNT:
                    card.setCostForTurn(card.costForTurn - 1);
                    break;
                case RETAIN:
                    card.retain = true;
                    break;
                case EXHAUST:
                    AbstractDungeon.player.hand.moveToExhaustPile(card);
                    addDraw();
                    break;
            }
        }, 1, effect == HandEffect.DISCOUNT);
    }

    public static CardSelection combatDiscount(RaceCard source, int amount) {
        return new CardSelection(() -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : AbstractDungeon.player.hand.group) {
                if (card != source) group.addToBottom(card);
            }
            return group;
        }, card -> card.updateCost(-amount), 1, false);
    }

    public static CardSelection shuffleDiscard(int count) {
        return new CardSelection(() -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.addAll(AbstractDungeon.player.discardPile.group);
            return group;
        }, card -> {
            AbstractDungeon.player.discardPile.removeCard(card);
            AbstractDungeon.player.drawPile.addToRandomSpot(card);
        }, count, true);
    }

    public static CardSelection inspectDraw(int inspect, int count, boolean optional, boolean toHand) {
        return new CardSelection(() -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            java.util.List<AbstractCard> draw = AbstractDungeon.player.drawPile.group;
            for (int i = draw.size() - 1; i >= Math.max(0, draw.size() - inspect); i--) {
                group.addToBottom(draw.get(i));
            }
            return group;
        }, card -> {
            if (toHand && AbstractDungeon.player.hand.size() >= BaseMod.MAX_HAND_SIZE) return;
            AbstractDungeon.player.drawPile.removeCard(card);
            if (toHand) AbstractDungeon.player.hand.addToTop(card);
            else AbstractDungeon.player.drawPile.addToBottom(card);
        }, count, optional);
    }

    public static CardSelection upgradeDrawPile() {
        return new CardSelection(() -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : AbstractDungeon.player.drawPile.group) {
                if (card.canUpgrade()) group.addToBottom(card);
            }
            return group;
        }, AbstractCard::upgrade, 2, true);
    }

    public static CardSelection recoverHalf(int count) {
        return new CardSelection(() -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            group.group.addAll(Stamina.restingCards());
            return group;
        }, card -> Stamina.recoverHalfToDraw((RaceCard) card), count, false);
    }

    public static CardSelection discardDrawn(java.util.List<AbstractCard> drawn) {
        java.util.List<AbstractCard> snapshot = new java.util.ArrayList<>(drawn);
        return new CardSelection(() -> {
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (AbstractCard card : snapshot) {
                if (AbstractDungeon.player.hand.contains(card)) group.addToBottom(card);
            }
            return group;
        }, card -> {
            AbstractDungeon.player.hand.moveToDiscardPile(card);
            card.triggerOnManualDiscard();
            GameActionManager.incrementDiscard(false);
        }, 1, false);
    }

    private static void addDraw() {
        AbstractDungeon.actionManager.addToTop(new DrawCardAction(AbstractDungeon.player, 1));
    }

    @Override
    public void update() {
        if (!opened) {
            CardGroup group = candidates.get();
            if (group.isEmpty()) {
                isDone = true;
                return;
            }
            String prompt = Settings.language == Settings.GameLanguage.ZHS ? "选择卡牌" : "Choose cards";
            AbstractDungeon.gridSelectScreen.open(group, Math.min(group.size(), limit), anyNumber, prompt);
            opened = true;
        } else if (!AbstractDungeon.isScreenUp) {
            AbstractDungeon.gridSelectScreen.selectedCards.forEach(apply);
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            AbstractDungeon.player.hand.refreshHandLayout();
            isDone = true;
        }
    }
}
