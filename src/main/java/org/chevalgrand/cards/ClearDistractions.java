package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class ClearDistractions extends RaceCard {
    public ClearDistractions() {
        super("ClearDistractions", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardSelection.fromHand(CardSelection.HandEffect.EXHAUST, this));
    }

    @Override
    public ClearDistractions makeCopy() {
        return new ClearDistractions();
    }
}
