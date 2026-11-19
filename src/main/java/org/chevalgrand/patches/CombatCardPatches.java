package org.chevalgrand.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.chevalgrand.mechanics.CardMoves;
import org.chevalgrand.powers.PowerHooks;

public final class CombatCardPatches {
    private CombatCardPatches() {
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class Replay {
        public static void Postfix(UseCardAction __instance, AbstractCard ___targetCard) {
            CardMoves.afterUse(__instance, ___targetCard);
        }
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class FinishAttack {
        public static void Postfix(UseCardAction __instance, AbstractCard ___targetCard) {
            CardMoves.finishAttack(__instance, ___targetCard);
        }
    }

    @SpirePatch(clz = DamageAction.class, method = "update")
    public static class ExtraHit {
        public static void Postfix(DamageAction __instance, DamageInfo ___info) {
            CardMoves.extraHit(__instance, ___info);
        }
    }

    @SpirePatch(clz = DamageAllEnemiesAction.class, method = "update")
    public static class ExtraGroupHit {
        public static void Postfix(DamageAllEnemiesAction __instance) {
            CardMoves.extraGroupHit(__instance);
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class DetectBlocks {
        public static void Prefix(AbstractPlayer __instance) {
            PowerHooks.beforePlayerDamage(__instance);
        }

        public static void Postfix(AbstractPlayer __instance, DamageInfo info) {
            PowerHooks.afterPlayerDamage(__instance, info);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class DetectOverkill {
        public static void Prefix(AbstractMonster __instance) {
            PowerHooks.beforeMonsterDamage(__instance);
        }

        public static void Postfix(AbstractMonster __instance, DamageInfo info) {
            PowerHooks.afterMonsterDamage(__instance, info);
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class ModifyIncomingPower {
        public static SpireReturn<Void> Prefix(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
            if (PowerActionState.checked.get(__instance)) return SpireReturn.Continue();
            PowerActionState.checked.set(__instance, true);
            return PowerHooks.modifyIncomingPower(__instance, ___powerToApply);
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = SpirePatch.CLASS)
    public static class PowerActionState {
        public static final SpireField<Boolean> checked = new SpireField<>(() -> false);
    }
}
