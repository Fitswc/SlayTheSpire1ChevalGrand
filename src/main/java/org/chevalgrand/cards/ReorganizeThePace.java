package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class ReorganizeThePace extends RaceCard {
    public ReorganizeThePace() {
        super("ReorganizeThePace", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 6);
        configureBlock(20, 4);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
    }

    @Override
    public ReorganizeThePace makeCopy() {
        return new ReorganizeThePace();
    }
}
