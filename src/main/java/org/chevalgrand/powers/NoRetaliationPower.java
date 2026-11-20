package org.chevalgrand.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.ChevalGrandSlayMod;

public final class NoRetaliationPower extends ChevalPower implements InvisiblePower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:NO_RETALIATION");

    public NoRetaliationPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "NoRetaliation");
        updateDescription();
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damage) {
        return !AbstractDungeon.actionManager.turnHasEnded && info.owner instanceof AbstractMonster
                && info.type != DamageInfo.DamageType.NORMAL ? 0 : damage;
    }

    @Override
    public void stackPower(int extra) {
        // Reapplying this power does not increase its prevention.
    }
}
