package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class NoRegretsEnding extends RaceCard {
    public NoRegretsEnding() {
        super("NoRegretsEnding", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(10, 10);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.NOREGRETS, magicNumber);
    }

    @Override
    public NoRegretsEnding makeCopy() {
        return new NoRegretsEnding();
    }
}
