package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class PrefectDefend extends RaceCard {
    public PrefectDefend() {
        super("PrefectDefend", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(16, 4);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.PERFECT, magicNumber);
    }

    @Override
    public PrefectDefend makeCopy() {
        return new PrefectDefend();
    }
}
