package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class ReviewThePace extends RaceCard {
    public ReviewThePace() {
        super("ReviewThePace", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 6);
        configureMagic(2, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        draw(player, magicNumber);
        addToBot(CardSelection.fromHand(CardSelection.HandEffect.RETAIN, this));
    }

    @Override
    public ReviewThePace makeCopy() {
        return new ReviewThePace();
    }
}
