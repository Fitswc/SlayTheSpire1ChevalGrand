package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.chevalgrand.ChevalGrandSlayMod;

public final class ForgeAheadPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:FORGE");

    public ForgeAheadPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "ForgeAhead");
        updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type == AbstractCard.CardType.ATTACK && AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .filter(played -> played.type == AbstractCard.CardType.ATTACK).count() == 3) {
            addToBot(new GainEnergyAction(1));
            damageAllEnemies(18);
        }
    }
}
