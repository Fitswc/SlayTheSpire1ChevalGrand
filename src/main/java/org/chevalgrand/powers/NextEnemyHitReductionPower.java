package org.chevalgrand.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.chevalgrand.ChevalGrandSlayMod;

public final class NextEnemyHitReductionPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:NEXT_ENEMY_HIT_REDUCTION");
    private boolean used;

    public NextEnemyHitReductionPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "NextEnemyHitReduction", "NextEnemyHitReduction-large");
        updateDescription();
    }

    int beforeBlock(DamageInfo info, int damage) {
        if (used || !PowerHooks.isEnemyAttack(info) || !AbstractDungeon.actionManager.turnHasEnded) return damage;
        used = true;
        removeSelf();
        return Math.max(0, damage - amount);
    }

    @Override
    public void atStartOfTurn() {
        if (!used) removeSelf();
    }
}
