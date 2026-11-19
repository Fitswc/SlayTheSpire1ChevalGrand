package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.CardMoves;
import org.chevalgrand.mechanics.Stamina;

public final class PourIntoOneMove extends RaceCard {
    public PourIntoOneMove() {
        super("PourIntoOneMove", CardRarity.RARE, CardType.SKILL, CardTarget.ENEMY, 1, 2);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardMoves.hand(this, c -> c.type == CardType.ATTACK && Stamina.has(c, 2),
                c -> CardMoves.play((RaceCard)c, 1, enemy, false, true)));
    }

    @Override
    public PourIntoOneMove makeCopy() {
        return new PourIntoOneMove();
    }
}
