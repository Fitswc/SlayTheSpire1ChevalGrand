package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.chevalgrand.ChevalGrandSlayMod;

public final class FlowingAssaultPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("FlowingAssault");

    public FlowingAssaultPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "FlowingAssault", "FlowingAssault-large");
        updateDescription();
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (AbstractDungeon.actionManager.turnHasEnded || card.type != AbstractCard.CardType.ATTACK) return;
        if (AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .filter(played -> played.type == AbstractCard.CardType.ATTACK).count() > 3) {
            flash();
            addToBot(new DrawCardAction((AbstractPlayer) owner, amount));
        }
    }
}
