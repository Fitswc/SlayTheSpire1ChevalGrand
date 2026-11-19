package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class SportsDrinkSupply extends RaceCard {
    public SportsDrinkSupply() {
        super("SportsDrinkSupply", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 1, 0);
        configureMagic(2, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new MakeTempCardInHandAction(new EnergyDrink(), magicNumber));
    }

    @Override
    public SportsDrinkSupply makeCopy() {
        return new SportsDrinkSupply();
    }
}
