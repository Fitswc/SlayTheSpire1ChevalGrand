package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import org.chevalgrand.ChevalGrandSlayMod;

public final class VigorPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:VIGOR");

    public VigorPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "ClamToFight");
        updateDescription();
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        return isAttackDamage(type, card) ? damage + amount : damage;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK) removeSelf();
    }
}
