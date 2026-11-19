package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import org.chevalgrand.ChevalGrandSlayMod;

public final class NoRegretsEndingPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:NOREGRETS");

    private int pending;

    public NoRegretsEndingPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "NoRegretsEnding");
        updateDescription();
    }

    void onRestEnter() {
        pending += amount;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        return isAttackDamage(type, card) ? damage + pending : damage;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) pending = 0;
    }
}
