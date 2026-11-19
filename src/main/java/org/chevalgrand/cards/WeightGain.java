package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class WeightGain extends RaceCard {
    public WeightGain() {
        super("WeightGain", CardRarity.SPECIAL, CardType.STATUS, CardTarget.NONE, 1, 0);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
    }

    @Override
    public WeightGain makeCopy() {
        return new WeightGain();
    }
}
