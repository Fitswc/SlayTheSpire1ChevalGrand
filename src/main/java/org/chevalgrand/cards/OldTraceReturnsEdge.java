package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.Stamina;

public final class OldTraceReturnsEdge extends RaceCard {
    public OldTraceReturnsEdge() {
        super("OldTraceReturnsEdge", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY, 1, 4);
        configureDamage(8, 3);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        for (int i = 0; i < Stamina.restingCards().size(); i++) hit(player, enemy, damage);
    }

    @Override
    public OldTraceReturnsEdge makeCopy() {
        return new OldTraceReturnsEdge();
    }
}
