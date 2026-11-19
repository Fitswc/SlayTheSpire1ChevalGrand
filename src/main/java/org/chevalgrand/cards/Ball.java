package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class Ball extends RaceCard {
    public Ball() {
        super("Ball", CardRarity.SPECIAL, CardType.ATTACK, CardTarget.ENEMY, 0, 0);
        configureDamage(6, 0);
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy, damage);
    }

    @Override
    public Ball makeCopy() {
        return new Ball();
    }
}
