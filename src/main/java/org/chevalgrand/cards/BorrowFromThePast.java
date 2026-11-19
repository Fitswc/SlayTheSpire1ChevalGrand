package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class BorrowFromThePast extends RaceCard {
    public BorrowFromThePast() {
        super("BorrowFromThePast", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.BORROW, 1);
    }

    @Override
    public BorrowFromThePast makeCopy() {
        return new BorrowFromThePast();
    }
}
