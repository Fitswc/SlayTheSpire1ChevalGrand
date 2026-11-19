package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class WinningShift extends RaceCard {
    public WinningShift() {
        super("WinningShift", CardRarity.RARE, CardType.SKILL, CardTarget.SELF, 0, 2);
        configureMagic(3, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new GainEnergyAction(4));
        draw(player, magicNumber);
        addToBot(new MakeTempCardInDrawPileAction(new Dazed(), 2, true, true));
    }

    @Override
    public WinningShift makeCopy() {
        return new WinningShift();
    }
}
