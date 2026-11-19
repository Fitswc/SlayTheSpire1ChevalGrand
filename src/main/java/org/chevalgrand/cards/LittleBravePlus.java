package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class LittleBravePlus extends RaceCard {
    public LittleBravePlus() {
        super("LittleBravePlus", CardRarity.SPECIAL, CardType.ATTACK, CardTarget.ENEMY, 2, 0);
        configureDamage(36, 2);
        configureBlock(24, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        hit(player, enemy, damage);
    }

    @Override
    public LittleBravePlus makeCopy() {
        return new LittleBravePlus();
    }
}
