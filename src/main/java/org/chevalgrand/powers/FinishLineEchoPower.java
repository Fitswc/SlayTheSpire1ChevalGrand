package org.chevalgrand.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.chevalgrand.ChevalGrandSlayMod;

public final class FinishLineEchoPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:FINISH");

    public FinishLineEchoPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "FinishLineEcho");
        updateDescription();
    }

    void onRestEnter() {
        if (!AbstractDungeon.actionManager.turnHasEnded) damageAllEnemies(amount);
    }
}
