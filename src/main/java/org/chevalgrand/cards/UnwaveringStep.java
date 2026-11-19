package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class UnwaveringStep extends RaceCard {
    public UnwaveringStep() {
        super("UnwaveringStep", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 1, 0);
        configureMagic(18, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new ApplyPowerAction(player, player, new RacePower(RacePower.Effect.UnwaveringStep, player, magicNumber)));
    }

    @Override
    public UnwaveringStep makeCopy() {
        return new UnwaveringStep();
    }
}
