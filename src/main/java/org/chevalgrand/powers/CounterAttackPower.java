package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.cards.RaceCard;

public final class CounterAttackPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:COUNTER");

    public CounterAttackPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "CounterAttack");
        updateDescription();
    }

    @Override
    public void atStartOfTurn() {
        removeSelf();
    }

    void onBlockedAttack() {
        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (target != null) {
            addToBot(new DamageAction(target, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), RaceCard.SLASH));
        }
    }
}
