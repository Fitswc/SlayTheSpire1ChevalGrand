package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.CounterAttackPower;

public final class CounterAttack extends RaceCard {
    public CounterAttack() {
        super("CounterAttack", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 0);
        configureBlock(10, 4);
        configureMagic(6, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        apply(new CounterAttackPower(player, magicNumber));
    }

    @Override
    public CounterAttack makeCopy() {
        return new CounterAttack();
    }
}
