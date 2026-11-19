package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class KeepSomeInReserve extends RaceCard {
    public KeepSomeInReserve() {
        super("KeepSomeInReserve", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.ENERGY, 1);
    }

    @Override
    public KeepSomeInReserve makeCopy() {
        return new KeepSomeInReserve();
    }
}
