package org.chevalgrand.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import org.chevalgrand.ChevalGrandSlayMod;

public final class EndlessStaminaPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:ENDLESS");

    public EndlessStaminaPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "EndlessStamina", "EndlessStamina-large");
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) removeSelf();
    }

    @Override
    public void stackPower(int extra) {
        // Reapplying does not extend the effect beyond this turn.
    }
}
