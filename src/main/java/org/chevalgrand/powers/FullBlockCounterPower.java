package org.chevalgrand.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.chevalgrand.ChevalGrandSlayMod;

public final class FullBlockCounterPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:FULL_COUNTER");

    public FullBlockCounterPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "NoConcession");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        removeSelf();
    }

    void onFullyBlockedAttack() {
        if (AbstractDungeon.actionManager.turnHasEnded) damageAllEnemies(amount);
    }
}
