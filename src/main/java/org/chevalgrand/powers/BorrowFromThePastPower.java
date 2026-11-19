package org.chevalgrand.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.mechanics.Stamina;

public final class BorrowFromThePastPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:BORROW");

    public BorrowFromThePastPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "BorrowFromThePast");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        int cards = Stamina.restingCards().size();
        if (cards > 0) gainBlock(2 * cards);
    }

    @Override
    public void stackPower(int extra) {
        // This ability only counts each resting card once.
    }
}
