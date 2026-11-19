package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class RenewTheContest extends RaceCard {
    public RenewTheContest() {
        super("RenewTheContest", CardRarity.RARE, CardType.SKILL, CardTarget.SELF, 1, 2);
        configureMagic(16, 4);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardMoves.rest(1, false, c -> c.type == CardType.ATTACK, c -> {
            if (Stamina.returnToHand(c, true, false)) c.nextAttackBonus += magicNumber;
        }));
    }

    @Override
    public RenewTheContest makeCopy() {
        return new RenewTheContest();
    }
}
