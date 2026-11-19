package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class WinningLine extends RaceCard {
    public WinningLine() {
        super("WinningLine", CardRarity.RARE, CardType.ATTACK, CardTarget.ENEMY, 2, 2);
        configureDamage(16, 4);
        configureMagic(8, 1);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        int oldBase = baseDamage;
        baseDamage += magicNumber * Math.min(4, previousAttacks());
        calculateCardDamage(enemy);
        hit(player, enemy, damage);
        baseDamage = oldBase;
    }

    @Override
    public WinningLine makeCopy() {
        return new WinningLine();
    }
}
