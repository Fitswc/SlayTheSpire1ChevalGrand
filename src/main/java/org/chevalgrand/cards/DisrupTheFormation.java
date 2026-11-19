package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public final class DisrupTheFormation extends RaceCard {
    public DisrupTheFormation() {
        super("DisrupTheFormation", CardRarity.COMMON, CardType.ATTACK, CardTarget.ENEMY, 1, 0);
        configureDamage(10, 3);
        configureMagic(2, 0);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy, damage);
        addToBot(new ApplyPowerAction(enemy, player, new WeakPower(enemy, magicNumber, false)));
    }

    @Override
    public DisrupTheFormation makeCopy() {
        return new DisrupTheFormation();
    }
}
