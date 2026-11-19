package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.WeakPower;

public final class UnwindMomentum extends RaceCard {
    public UnwindMomentum() {
        super("UnwindMomentum", CardRarity.COMMON, CardType.SKILL, CardTarget.ENEMY, 1, 0);
        configureBlock(16, 2);
        configureMagic(6, 0);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        addToBot(new ApplyPowerAction(enemy, player, new WeakPower(enemy, magicNumber, false)));
    }

    @Override
    public UnwindMomentum makeCopy() {
        return new UnwindMomentum();
    }
}
