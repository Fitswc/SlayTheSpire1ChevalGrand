package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.NoPositivePower;

public final class SuppressTheirSpirit extends RaceCard {
    public SuppressTheirSpirit() {
        super("SuppressTheirSpirit", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY, 1, 0);
        configureDamage(8, 3);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy, damage);
        apply(new NoPositivePower(enemy, 1));
    }

    @Override
    public SuppressTheirSpirit makeCopy() {
        return new SuppressTheirSpirit();
    }
}
