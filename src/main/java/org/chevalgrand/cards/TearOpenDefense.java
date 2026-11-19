package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import com.megacrit.cardcrawl.powers.WeakPower;

public final class TearOpenDefense extends RaceCard {
    public TearOpenDefense() {
        super("TearOpenDefense", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY, 1, 4);
        configureDamage(12, 6);
        configureMagic(2, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy, damage);
        if (enemy.hasPower(VulnerablePower.POWER_ID) || enemy.hasPower(WeakPower.POWER_ID)) draw(player, magicNumber);
    }

    @Override
    public TearOpenDefense makeCopy() {
        return new TearOpenDefense();
    }
}
