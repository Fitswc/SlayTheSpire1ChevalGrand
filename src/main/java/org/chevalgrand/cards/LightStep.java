package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class LightStep extends RaceCard {
    public LightStep() {
        super("LightStep", CardRarity.COMMON, CardType.ATTACK, CardTarget.ENEMY, 0, 6);
        configureDamage(10, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy, damage);
    }

    @Override
    public LightStep makeCopy() {
        return new LightStep();
    }
}
