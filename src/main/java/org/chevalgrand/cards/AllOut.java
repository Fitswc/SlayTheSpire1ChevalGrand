package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.CardMoves;
import org.chevalgrand.mechanics.Stamina;

public final class AllOut extends RaceCard {
    public AllOut() {
        super("AllOut", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 3, 4);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardMoves.hand(this, c -> Stamina.has(c, 1), c -> CardMoves.play((RaceCard)c, ((RaceCard)c).remaining, null, false, false)));
    }

    @Override
    public AllOut makeCopy() {
        return new AllOut();
    }
}
