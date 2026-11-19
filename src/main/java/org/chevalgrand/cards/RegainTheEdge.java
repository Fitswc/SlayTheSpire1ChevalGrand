package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.CardMoves;

public final class RegainTheEdge extends RaceCard {
    public RegainTheEdge() {
        super("RegainTheEdge", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ALL_ENEMY, -1, 6);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        int count = xEnergy(player);
        addToBot(CardMoves.cleanse(this, count));
    }

    @Override
    public RegainTheEdge makeCopy() {
        return new RegainTheEdge();
    }
}
