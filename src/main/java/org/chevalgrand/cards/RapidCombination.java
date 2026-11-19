package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class RapidCombination extends RaceCard {
    public RapidCombination() {
        super("RapidCombination", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY, 1, 4);
        configureDamage(10, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        for (int i = 0; i < 3; i++) hit(player, enemy, damage);
    }

    @Override
    public RapidCombination makeCopy() {
        return new RapidCombination();
    }
}
