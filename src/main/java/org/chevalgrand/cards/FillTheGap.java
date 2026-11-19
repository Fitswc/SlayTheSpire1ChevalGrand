package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class FillTheGap extends RaceCard {
    public FillTheGap() {
        super("FillTheGap", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 1, 0);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.FILL, magicNumber);
    }

    @Override
    public FillTheGap makeCopy() {
        return new FillTheGap();
    }
}
