package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class BorrowForceEscape extends RaceCard {
    public BorrowForceEscape() {
        super("BorrowForceEscape", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(2, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.RESIST, magicNumber);
    }

    @Override
    public BorrowForceEscape makeCopy() {
        return new BorrowForceEscape();
    }
}
