package org.chevalgrand.mechanics;

import basemod.abstracts.CustomSavable;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.ArrayList;
import java.util.Comparator;
import org.chevalgrand.cards.EmptyShell;
import org.chevalgrand.cards.RaceCard;
import org.chevalgrand.powers.PowerHooks;
import org.chevalgrand.relics.ChevalGrandHatPlus;

public final class Stamina implements CustomSavable<int[][]> {
    private static final ArrayList<RaceCard> REST = new ArrayList<>();
    private static int restOrder;
    private static int lastRestTurn = -1;

    public static java.util.List<RaceCard> restingCards() {
        return java.util.Collections.unmodifiableList(REST);
    }

    public static void restoreAfterUpgrade(RaceCard card) {
        card.setRemaining(card.maxStamina());
        card.clearRestOrder();
        if (REST.remove(card)) AbstractDungeon.player.drawPile.addToTop(card);
    }

    public static boolean restedThisTurn() {
        return lastRestTurn == GameActionManager.turn;
    }

    public static boolean has(AbstractCard card, int minimum) {
        return card instanceof RaceCard && ((RaceCard) card).baseStamina() > 0
                && ((RaceCard) card).remainingStamina() >= minimum;
    }

    public static boolean canRestoreDeckCard(AbstractCard card) {
        return has(card, 0) && ((RaceCard) card).remainingStamina() < ((RaceCard) card).maxStamina();
    }

    public static void applyHatBonus(CardGroup pile, AbstractCard card) {
        com.megacrit.cardcrawl.characters.AbstractPlayer player = AbstractDungeon.player;
        if (player != null && (pile == player.hand || pile == player.drawPile || pile == player.discardPile)
                && card instanceof RaceCard && !player.masterDeck.contains(card)
                && player.hasRelic(ChevalGrandHatPlus.ID)) {
            ((RaceCard) card).applyHatBonus();
        }
    }

    public static boolean returnToHand(RaceCard card, boolean fill, boolean discount) {
        if (!REST.contains(card)) return false;
        if (fill) card.setRemaining(card.maxStamina());
        if (AbstractDungeon.player.hand.size() >= basemod.BaseMod.MAX_HAND_SIZE) return false;
        leaveRest(card);
        AbstractDungeon.player.hand.addToTop(card);
        if (discount && card.cost >= 0) card.setCostForTurn(card.costForTurn - 1);
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
        return true;
    }

    public static void leaveRest(RaceCard card) {
        if (REST.remove(card)) {
            card.clearRestOrder();
            PowerHooks.onRestExit(AbstractDungeon.player, card);
        }
    }

    public static void restoreAll() {
        for (RaceCard card : new ArrayList<>(REST)) {
            moveToDraw(card, card.maxStamina());
        }
    }

    public static void restoreToHand(RaceCard card) {
        returnToHand(card, true, true);
    }

    public static void moveToDraw(RaceCard card, int amount) {
        if (!REST.contains(card)) {
            return;
        }
        card.setRemaining(Math.min(card.remainingStamina() + amount, card.maxStamina()));
        if (card.remainingStamina() == card.maxStamina()) {
            leaveRest(card);
            AbstractDungeon.player.drawPile.addToTop(card);
        }
    }

    public static boolean moveDepletedToRest(CardGroup pile, AbstractCard card) {
        if (card instanceof RaceCard && AbstractDungeon.player != null
                && pile == AbstractDungeon.player.hand) {
            RaceCard race = (RaceCard) card;
            if (race.maxStamina() > 0 && race.remainingStamina() == 0) {
                pile.removeCard(card);
                AbstractDungeon.player.limbo.removeCard(card);
                card.unhover();
                card.untip();
                card.stopGlowing();
                if (!REST.contains(race)) {
                    race.rememberRestOrder(++restOrder);
                    REST.add(race);
                    lastRestTurn = GameActionManager.turn;
                    PowerHooks.onRestEnter(AbstractDungeon.player);
                }
                AbstractDungeon.player.onCardDrawOrDiscard();
                pile.refreshHandLayout();
                return true;
            }
        }
        return false;
    }

    public static void recoverHalfToDraw(RaceCard card) {
        if (REST.contains(card)) {
            card.setRemaining(card.remainingStamina() + (card.maxStamina() + 1) / 2);
            leaveRest(card);
            AbstractDungeon.player.drawPile.addToTop(card);
        }
    }

    public static AbstractCard copyStamina(AbstractCard copy, AbstractCard source) {
        if (copy instanceof RaceCard && source instanceof RaceCard) {
            RaceCard original = (RaceCard) source;
            RaceCard destination = (RaceCard) copy;
            boolean inDeck = AbstractDungeon.player != null && AbstractDungeon.player.masterDeck.contains(original);
            if (!inDeck && original.maxStamina() > 0 && original.remainingStamina() == 0) {
                return new EmptyShell();
            }
            destination.copyCombatStamina(original);
            destination.rememberRestOrder(original.restOrder());
            if (inDeck) {
                destination.linkDeckOrigin(original);
            }
        }
        return copy;
    }

    public static void initializeCombat(CardGroup drawPile) {
        PowerHooks.resetCombat();
        REST.clear();
        lastRestTurn = -1;
        restOrder = 0;
        for (AbstractCard card : new ArrayList<>(drawPile.group)) {
            if (card instanceof RaceCard) {
                RaceCard race = (RaceCard) card;
                applyHatBonus(drawPile, race);
                if (race.baseStamina() > 0 && (race.remainingStamina() == 0 ||
                        race.restOrder() > 0 && race.remainingStamina() < race.maxStamina())) {
                    drawPile.removeCard(race);
                    REST.add(race);
                    restOrder = Math.max(restOrder, race.restOrder());
                }
            }
        }
        REST.sort(Comparator.comparingInt(card -> card.restOrder()));
    }

    @Override
    public int[][] onSave() {
        if (AbstractDungeon.player == null) {
            return null;
        }
        ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        int[][] result = new int[cards.size()][];
        for (int i = 0; i < cards.size(); i++) {
            if (cards.get(i) instanceof RaceCard) {
                RaceCard card = (RaceCard) cards.get(i);
                if (card.maxStamina() > 0) {
                    result[i] = new int[] {card.remainingStamina(), card.restOrder()};
                }
            }
        }
        return result;
    }

    @Override
    public void onLoad(int[][] values) {
        if (values == null || AbstractDungeon.player == null) {
            return;
        }
        ArrayList<AbstractCard> cards = AbstractDungeon.player.masterDeck.group;
        for (int i = 0; i < Math.min(values.length, cards.size()); i++) {
            if (values[i] != null && values[i].length == 2 && cards.get(i) instanceof RaceCard) {
                RaceCard card = (RaceCard) cards.get(i);
                card.setRemaining(values[i][0]);
                card.rememberRestOrder(Math.max(0, values[i][1]));
            }
        }
    }
}
