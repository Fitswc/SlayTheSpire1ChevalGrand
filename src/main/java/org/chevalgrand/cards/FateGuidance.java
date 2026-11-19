package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class FateGuidance extends RaceCard {
    public FateGuidance() {
        super("FateGuidance", CardRarity.RARE, CardType.SKILL, CardTarget.SELF, 1, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardSelection.fromRest(4, false, true, true));
    }

    @Override
    public FateGuidance makeCopy() {
        return new FateGuidance();
    }
}
