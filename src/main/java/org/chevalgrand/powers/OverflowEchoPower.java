package org.chevalgrand.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import org.chevalgrand.ChevalGrandSlayMod;

public final class OverflowEchoPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:OVERFLOW");

    public OverflowEchoPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "EchoOfTheFinish");
        updateDescription();
    }

    void onOverkill(int excessDamage) {
        int damage = Math.min(amount, excessDamage);
        if (damage > 0) damageAllEnemies(damage);
    }
}
