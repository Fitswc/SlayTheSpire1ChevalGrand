package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class EnergyDrink extends RaceCard {
    public EnergyDrink() {
        super("EnergyDrink", CardRarity.SPECIAL, CardType.SKILL, CardTarget.SELF, 0, 0);
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new HealAction(player, player, 6));
        long drinks = AbstractDungeon.actionManager.cardsPlayedThisCombat.stream().filter(card -> cardID.equals(card.cardID)).count();
        if (drinks > 0 && drinks % 6 == 0) addToBot(new MakeTempCardInHandAction(new WeightGain()));
    }

    @Override
    public EnergyDrink makeCopy() {
        return new EnergyDrink();
    }
}
