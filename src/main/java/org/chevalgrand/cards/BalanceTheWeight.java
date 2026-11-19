package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class BalanceTheWeight extends RaceCard {
    public BalanceTheWeight() {
        super("BalanceTheWeight", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 0, 0);
        configureMagic(2, 1);
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new GainEnergyAction(magicNumber));
        addToBot(new MakeTempCardInHandAction(new WeightGain()));
    }

    @Override
    public BalanceTheWeight makeCopy() {
        return new BalanceTheWeight();
    }
}
