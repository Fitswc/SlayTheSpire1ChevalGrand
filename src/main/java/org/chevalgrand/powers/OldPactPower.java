package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.chevalgrand.ChevalGrandSlayMod;

public final class OldPactPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:PACT");

    public OldPactPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "RenewTheOldPact");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = description(amount >= 2 ? 1 : 0);
    }

    @Override
    public void atStartOfTurn() {
        addToBot(new GainEnergyAction(1));
        addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 4)));
        if (amount >= 2) addToBot(new DrawCardAction(1));
    }

    @Override
    public void stackPower(int extra) {
        amount = Math.max(amount, extra);
        updateDescription();
    }
}
