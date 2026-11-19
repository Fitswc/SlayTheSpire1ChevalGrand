package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.cards.RaceCard;

public final class RemainingUsesSensePower extends ChevalPower implements PowerHooks.AfterRestEnter {
    public static final String POWER_ID = ChevalGrandSlayMod.id("RemainingUsesSense");

    public RemainingUsesSensePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "RemainingUsesSense", "RemainingUsesSense-large");
        updateDescription();
    }

    @Override
    public void afterRestEnter() {
        if (AbstractDungeon.actionManager.turnHasEnded) return;
        AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
        if (target != null) {
            addToBot(new DamageAction(target, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS), RaceCard.SLASH));
        }
    }
}
