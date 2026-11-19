package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.FillTheGapPower;

public final class FillTheGap extends RaceCard {
    public FillTheGap() {
        super("FillTheGap", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 1, 0);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new FillTheGapPower(player, magicNumber));
    }

    @Override
    public FillTheGap makeCopy() {
        return new FillTheGap();
    }
}
