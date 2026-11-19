package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class RewriteFate extends RaceCard {
    public RewriteFate() {
        super("RewriteFate", CardRarity.RARE, CardType.SKILL, CardTarget.SELF, 2, 2);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardMoves.rest(4, true, c -> true, c -> {
            c.setRemaining(c.maxStamina);
            CardMoves.play(c, 1, null, true, false);
        }));
    }

    @Override
    public RewriteFate makeCopy() {
        return new RewriteFate();
    }
}
