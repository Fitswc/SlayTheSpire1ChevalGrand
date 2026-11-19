package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.chevalgrand.ChevalGrandSlayMod;

public final class WinningRhythmPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("WinningRhythm");

    public WinningRhythmPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "WinningRhythm", "WinningRhythm-large");
        updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (!AbstractDungeon.actionManager.turnHasEnded && card.type == AbstractCard.CardType.SKILL) {
            flash();
            gainBlock(amount);
        }
    }
}
