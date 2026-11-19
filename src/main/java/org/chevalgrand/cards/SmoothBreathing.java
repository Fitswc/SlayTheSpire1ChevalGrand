package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class SmoothBreathing extends RaceCard {
    public SmoothBreathing() {
        super("SmoothBreathing", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 0, 4);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        if (player.drawPile.isEmpty()) addToBot(new EmptyDeckShuffleAction());
        addToBot(CardSelection.inspectDraw(4, magicNumber, false, true));
    }

    @Override
    public SmoothBreathing makeCopy() {
        return new SmoothBreathing();
    }
}
