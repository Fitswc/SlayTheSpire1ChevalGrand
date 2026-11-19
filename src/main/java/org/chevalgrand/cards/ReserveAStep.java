package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class ReserveAStep extends RaceCard {
    public ReserveAStep() {
        super("ReserveAStep", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 6);
        configureBlock(5, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        addToBot(CardMoves.hand(this, c -> Stamina.has(c, 1), c -> ((RaceCard)c).preserveStamina = true));
    }

    @Override
    public ReserveAStep makeCopy() {
        return new ReserveAStep();
    }
}
