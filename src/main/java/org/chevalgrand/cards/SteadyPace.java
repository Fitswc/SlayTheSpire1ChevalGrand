package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class SteadyPace extends RaceCard {
    public SteadyPace() {
        super("SteadyPace", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 1, 4);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, player.currentBlock);
    }

    @Override
    public SteadyPace makeCopy() {
        return new SteadyPace();
    }
}
