package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class WinningRhythm extends RaceCard {
    public WinningRhythm() {
        super("WinningRhythm", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(10, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new ApplyPowerAction(player, player, new RacePower(RacePower.Effect.WinningRhythm, player, magicNumber)));
    }

    @Override
    public WinningRhythm makeCopy() {
        return new WinningRhythm();
    }
}
