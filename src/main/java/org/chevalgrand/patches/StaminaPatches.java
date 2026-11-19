package org.chevalgrand.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatches;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import org.chevalgrand.mechanics.Stamina;

public final class StaminaPatches {
    private StaminaPatches() {
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToDiscardPile")
    public static class Discard {
        public static SpireReturn<Void> Prefix(CardGroup __instance, AbstractCard c) {
            return Stamina.moveDepletedToRest(__instance, c) ? SpireReturn.Return(null) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "moveToExhaustPile")
    public static class Exhaust {
        public static SpireReturn<Void> Prefix(CardGroup __instance, AbstractCard c) {
            return Stamina.moveDepletedToRest(__instance, c) ? SpireReturn.Return(null) : SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = AbstractCard.class, method = "makeStatEquivalentCopy")
    public static class Copy {
        public static AbstractCard Postfix(AbstractCard __result, AbstractCard __instance) {
            return Stamina.copyStamina(__result, __instance);
        }
    }

    @SpirePatch(clz = CardGroup.class, method = "initializeDeck")
    public static class LoadCombat {
        public static void Postfix(CardGroup __instance) {
            Stamina.initializeCombat(__instance);
        }
    }

    @SpirePatches({
            @SpirePatch(clz = CardGroup.class, method = "addToHand"),
            @SpirePatch(clz = CardGroup.class, method = "addToTop"),
            @SpirePatch(clz = CardGroup.class, method = "addToBottom"),
            @SpirePatch(clz = CardGroup.class, method = "addToRandomSpot")
    })
    public static class EnterCombatPile {
        public static void Prefix(CardGroup __instance, AbstractCard __0) {
            Stamina.applyHatBonus(__instance, __0);
        }
    }
}
