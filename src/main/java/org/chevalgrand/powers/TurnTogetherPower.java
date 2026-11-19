package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import org.chevalgrand.ChevalGrandSlayMod;

public final class TurnTogetherPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:TOGETHER");

    public TurnTogetherPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "TurnTogether");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = description(amount > 0 ? 1 : 0);
    }

    void onRestExit() {
        addToBot(new DrawCardAction(1));
        if (amount > 0) gainBlock(amount);
    }

    @Override
    public void stackPower(int extra) {
        amount = Math.max(amount, extra);
        updateDescription();
    }
}
