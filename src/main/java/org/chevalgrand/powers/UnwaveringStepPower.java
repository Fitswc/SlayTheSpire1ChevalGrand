package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.chevalgrand.ChevalGrandSlayMod;

public final class UnwaveringStepPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("UnwaveringStep");

    public UnwaveringStepPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "UnwaveringStep", "UnwaveringStep-large");
        updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (!AbstractDungeon.actionManager.turnHasEnded && card.type == AbstractCard.CardType.ATTACK && card.cost >= 1) {
            flash();
            gainBlock(amount);
        }
    }
}
