package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class HandoverMoment extends RaceCard {
    public HandoverMoment() {
        super("HandoverMoment", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 0, 6);
        configureMagic(2, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        draw(player, magicNumber);
        addToBot(new GainEnergyAction(2));
    }

    @Override
    public HandoverMoment makeCopy() {
        return new HandoverMoment();
    }
}
