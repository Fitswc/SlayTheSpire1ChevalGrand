package org.chevalgrand.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.chevalgrand.ChevalGrandSlayMod;

public final class NoPositivePower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:NO_POSITIVE");

    public NoPositivePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "SuppressTheirSpirit");
        type = PowerType.DEBUFF;
        updateDescription();
    }

    boolean blocks(AbstractPower incoming) {
        return incoming.type == PowerType.BUFF;
    }
}
