package org.chevalgrand.powers;

import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.ArrayDeque;
import org.chevalgrand.cards.RaceCard;
import org.chevalgrand.mechanics.CardMoves;

/** Dispatches game events that have no native AbstractPower callback. */
public final class PowerHooks {
    private static final ArrayDeque<Integer> BEFORE_BLOCK = new ArrayDeque<>();
    private static final ArrayDeque<Integer> BEFORE_MONSTER_HP = new ArrayDeque<>();

    private PowerHooks() {}

    interface AfterRestEnter {
        void afterRestEnter();
    }

    public static void resetCombat() {
        WideViewPower.resetHandLimit();
        CardMoves.clear();
    }

    public static void onRestEnter(AbstractPlayer player) {
        NoRegretsEndingPower noRegrets = find(player, NoRegretsEndingPower.POWER_ID, NoRegretsEndingPower.class);
        if (noRegrets != null) noRegrets.onRestEnter();
        FinishLineEchoPower finish = find(player, FinishLineEchoPower.POWER_ID, FinishLineEchoPower.class);
        if (finish != null) finish.onRestEnter();
        for (AbstractPower power : player.powers) {
            if (power instanceof AfterRestEnter) ((AfterRestEnter) power).afterRestEnter();
        }
    }

    public static void onRestExit(AbstractPlayer player, RaceCard card) {
        if (AbstractDungeon.actionManager.turnHasEnded) return;
        FillTheGapPower fill = find(player, FillTheGapPower.POWER_ID, FillTheGapPower.class);
        if (fill != null) fill.onRestExit(card);
        TurnTogetherPower together = find(player, TurnTogetherPower.POWER_ID, TurnTogetherPower.class);
        if (together != null) together.onRestExit();
    }

    static boolean isEnemyAttack(DamageInfo info) {
        return info.type == DamageInfo.DamageType.NORMAL && info.owner instanceof AbstractMonster;
    }

    public static void beforePlayerDamage(AbstractPlayer player) {
        BEFORE_BLOCK.push(player.currentBlock);
    }

    public static void afterPlayerDamage(AbstractPlayer player, DamageInfo info) {
        int previousBlock = BEFORE_BLOCK.pop();
        if (!isEnemyAttack(info) || previousBlock <= player.currentBlock) return;
        CounterAttackPower counter = find(player, CounterAttackPower.POWER_ID, CounterAttackPower.class);
        if (counter != null) counter.onBlockedAttack();
        if (player.lastDamageTaken != 0) return;
        PerfectDefendPower perfect = find(player, PerfectDefendPower.POWER_ID, PerfectDefendPower.class);
        if (perfect != null) perfect.onFullyBlockedAttack();
        FullBlockCounterPower full = find(player, FullBlockCounterPower.POWER_ID, FullBlockCounterPower.class);
        if (full != null) full.onFullyBlockedAttack();
    }

    public static void beforeMonsterDamage(AbstractMonster monster) {
        BEFORE_MONSTER_HP.push(monster.currentHealth);
    }

    public static void afterMonsterDamage(AbstractMonster monster, DamageInfo info) {
        int previousHealth = BEFORE_MONSTER_HP.pop();
        AbstractPlayer player = AbstractDungeon.player;
        if (player == null || info.owner != player || previousHealth <= 0 || monster.currentHealth > 0) return;
        OverflowEchoPower overflow = find(player, OverflowEchoPower.POWER_ID, OverflowEchoPower.class);
        if (overflow != null) overflow.onOverkill(monster.lastDamageTaken - previousHealth);
    }

    public static SpireReturn<Void> modifyIncomingPower(ApplyPowerAction action, AbstractPower incoming) {
        AbstractCreature target = action.target;
        if (action.isDone || target == null || incoming == null || action.amount <= 0) return SpireReturn.Continue();
        NoPositivePower noPositive = find(target, NoPositivePower.POWER_ID, NoPositivePower.class);
        if (noPositive != null && noPositive.blocks(incoming)) {
            action.isDone = true;
            return SpireReturn.Return();
        }
        ResistWeakFrailPower resist = find(target, ResistWeakFrailPower.POWER_ID, ResistWeakFrailPower.class);
        if (resist != null && resist.appliesTo(incoming)) {
            action.amount = resist.reduceStacks(action.amount);
            incoming.amount = action.amount;
            if (action.amount == 0) {
                action.isDone = true;
                return SpireReturn.Return();
            }
        }
        if (incoming instanceof WideViewPower) {
            WideViewPower wideView = find(target, WideViewPower.POWER_ID, WideViewPower.class);
            if (wideView != null) wideView.mergeRecovery((WideViewPower) incoming);
        }
        return SpireReturn.Continue();
    }

    private static <T extends AbstractPower> T find(AbstractCreature owner, String id, Class<T> type) {
        AbstractPower power = owner.getPower(id);
        return type.isInstance(power) ? type.cast(power) : null;
    }
}
