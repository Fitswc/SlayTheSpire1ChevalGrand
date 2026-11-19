package org.chevalgrand.cards;

import basemod.abstracts.CustomSavable;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import java.util.ArrayList;
import java.util.Comparator;

final class Stamina implements CustomSavable<int[][]> {
    static final ArrayList<RaceCard> REST = new ArrayList<>();
    private static int restOrder;
    static int lastRestTurn = -1;

    static boolean restedThisTurn() {
        return lastRestTurn == GameActionManager.turn;
    }

    static boolean has(AbstractCard card, int minimum) {
        return card instanceof RaceCard && ((RaceCard)card).maxStamina > 0 && ((RaceCard)card).remaining >= minimum;
    }

    static boolean returnToHand(RaceCard card, boolean fill, boolean discount) {
        if (!REST.contains(card) || AbstractDungeon.player.hand.size() >= basemod.BaseMod.MAX_HAND_SIZE) return false;
        if (fill) card.setRemaining(card.maxStamina);
        leaveRest(card);
        AbstractDungeon.player.hand.addToTop(card);
        if (discount && card.cost >= 0) card.setCostForTurn(card.costForTurn - 1);
        AbstractDungeon.player.hand.refreshHandLayout();
        AbstractDungeon.player.hand.applyPowers();
        return true;
    }

    static void leaveRest(RaceCard card) {
        if (REST.remove(card)) {
            card.clearRestOrder();
            RaceAbility.onRestExit(card);
        }
    }

    static void restoreAll() {
        for (RaceCard card : new ArrayList<>(REST)) {
            moveToDraw(card, card.maxStamina);
        }
    }

    static void restoreToHand(RaceCard card) {
        returnToHand(card, true, true);
    }

    static void moveToDraw(RaceCard card, int amount) {
        if (!REST.contains(card)) {
            return;
        }
        card.setRemaining(Math.min(card.remaining + amount, card.maxStamina));
        if (card.remaining == card.maxStamina) {
            leaveRest(card);
            AbstractDungeon.player.drawPile.addToTop(card);
        }
    }

    private static SpireReturn<Void> intercept(CardGroup pile, AbstractCard card) {
        if (card instanceof RaceCard && AbstractDungeon.player != null
                && pile == AbstractDungeon.player.hand) {
            RaceCard race = (RaceCard) card;
            if (race.maxStamina > 0 && race.remaining == 0) {
                pile.removeCard(card);
                AbstractDungeon.player.limbo.removeCard(card);
                card.unhover();
                card.untip();
                card.stopGlowing();
                if (!REST.contains(race)) {
                    race.rememberRestOrder(++restOrder);
                    REST.add(race);
                    lastRestTurn = GameActionManager.turn;
                    RaceAbility.onRestEnter();
                    for (com.megacrit.cardcrawl.powers.AbstractPower power : AbstractDungeon.player.powers) {
                        if (power instanceof RacePower) ((RacePower) power).onCardRested();
                    }
                }
                AbstractDungeon.player.onCardDrawOrDiscard();
                pile.refreshHandLayout();
                return SpireReturn.Return(null);
            }
        }
        return SpireReturn.Continue();
    }

    static void recoverHalfToDraw(RaceCard card) {
        if (REST.contains(card)) {
            card.setRemaining(card.remaining + (card.maxStamina + 1) / 2);
            leaveRest(card);
            AbstractDungeon.player.drawPile.addToTop(card);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToDiscardPile")
    public static class Discard {
        public static SpireReturn<Void> Prefix(CardGroup __instance, AbstractCard c) {
            return intercept(__instance, c);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile")
    public static class Exhaust {
        public static SpireReturn<Void> Prefix(CardGroup __instance, AbstractCard c) {
            return intercept(__instance, c);
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class Copy {
        public static AbstractCard Postfix(AbstractCard __result, AbstractCard __instance) {
            if (__result instanceof RaceCard && __instance instanceof RaceCard) {
                RaceCard original = (RaceCard) __instance;
                RaceCard copy = (RaceCard) __result;
                boolean inDeck = AbstractDungeon.player != null && AbstractDungeon.player.masterDeck.contains(original);
                if (!inDeck && original.maxStamina > 0 && original.remaining == 0) {
                    return new EmptyShell();
                }
                copy.setRemaining(original.remaining);
                copy.restIndex = original.restIndex;
                if (inDeck) {
                    copy.deckOrigin = original;
                }
            }
            return __result;
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "initializeDeck")
    public static class LoadCombat {
        public static void Postfix(CardGroup __instance) {
            RaceAbility.resetCombat();
            REST.clear();
            lastRestTurn = -1;
            restOrder = 0;
            for (AbstractCard card : new ArrayList<>(__instance.group)) {
                if (card instanceof RaceCard) {
                    RaceCard race = (RaceCard) card;
                    if (race.maxStamina > 0 && (race.remaining == 0 || race.restIndex > 0)) {
                        __instance.removeCard(race);
                        REST.add(race);
                        restOrder = Math.max(restOrder, race.restIndex);
                    }
                }
            }
            REST.sort(Comparator.comparingInt(card -> card.restIndex));
        }
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
                if (card.maxStamina > 0) {
                    result[i] = new int[] {card.remaining, card.restIndex};
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
                card.restIndex = Math.max(0, values[i][1]);
            }
        }
    }
}
