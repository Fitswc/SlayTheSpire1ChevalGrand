package org.chevalgrand.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import org.chevalgrand.ChevalGrandSlayMod;

public final class OldRoadWarmthPower extends ChevalPower implements PowerHooks.AfterRestEnter {
    public static final String POWER_ID = ChevalGrandSlayMod.id("OldRoadWarmth");

    public OldRoadWarmthPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "OldRoadWarmth", "OldRoadWarmth-large");
        updateDescription();
    }

    @Override
    public void afterRestEnter() {
        gainBlock(amount);
    }
}
