package org.chevalgrand.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.WeakPower;
import org.chevalgrand.ChevalGrandSlayMod;

public final class ResistWeakFrailPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:RESIST");

    public ResistWeakFrailPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "BorrowForceEscape");
        updateDescription();
    }

    boolean appliesTo(AbstractPower incoming) {
        return WeakPower.POWER_ID.equals(incoming.ID) || FrailPower.POWER_ID.equals(incoming.ID);
    }

    int reduceStacks(int stacks) {
        return Math.max(0, stacks - amount);
    }
}
