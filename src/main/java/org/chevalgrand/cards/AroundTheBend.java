package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.CardSelection;

public final class AroundTheBend extends RaceCard {
    public AroundTheBend() {
        super("AroundTheBend", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 0);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        draw(player, magicNumber);
        addToBot(CardSelection.fromHand(CardSelection.HandEffect.DISCOUNT, this));
    }

    @Override
    public AroundTheBend makeCopy() {
        return new AroundTheBend();
    }
}
