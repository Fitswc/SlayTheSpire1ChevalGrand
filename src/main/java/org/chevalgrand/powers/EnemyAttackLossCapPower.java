package org.chevalgrand.powers;

import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.chevalgrand.ChevalGrandSlayMod;

public final class EnemyAttackLossCapPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:CAP");

    private int loss;

    public EnemyAttackLossCapPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "MeasuredAdvanceAndRetreat");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        removeSelf();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (!PowerHooks.isEnemyAttack(info) || !AbstractDungeon.actionManager.turnHasEnded) return damageAmount;
        int actual = Math.min(damageAmount, Math.max(0, amount - loss));
        loss += actual;
        return actual;
    }
}
