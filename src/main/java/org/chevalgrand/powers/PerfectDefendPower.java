package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import org.chevalgrand.ChevalGrandSlayMod;

public final class PerfectDefendPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:PERFECT");

    public PerfectDefendPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "PrefectDefend");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        if (amount > 0) gainBlock(amount);
    }

    void onFullyBlockedAttack() {
        addToBot(new GainEnergyAction(1));
        addToBot(new DrawCardAction(1));
    }
}
