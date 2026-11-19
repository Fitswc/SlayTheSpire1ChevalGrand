package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.UnwaveringStepPower;

public final class UnwaveringStep extends RaceCard {
    public UnwaveringStep() {
        super("UnwaveringStep", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 1, 0);
        configureMagic(18, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new UnwaveringStepPower(player, magicNumber));
    }

    @Override
    public UnwaveringStep makeCopy() {
        return new UnwaveringStep();
    }
}
