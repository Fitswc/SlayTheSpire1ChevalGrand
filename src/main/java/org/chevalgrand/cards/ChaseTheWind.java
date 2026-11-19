package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.CardMoves;

public final class ChaseTheWind extends RaceCard {
    public ChaseTheWind() {
        super("ChaseTheWind", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY, 1, 4);
        configureDamage(10, 3);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy, damage);
        addToBot(CardMoves.choosePile(magicNumber));
    }

    @Override
    public ChaseTheWind makeCopy() {
        return new ChaseTheWind();
    }
}
