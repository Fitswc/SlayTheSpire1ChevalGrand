package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.BorrowFromThePastPower;

public final class BorrowFromThePast extends RaceCard {
    public BorrowFromThePast() {
        super("BorrowFromThePast", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new BorrowFromThePastPower(player, 1));
    }

    @Override
    public BorrowFromThePast makeCopy() {
        return new BorrowFromThePast();
    }
}
