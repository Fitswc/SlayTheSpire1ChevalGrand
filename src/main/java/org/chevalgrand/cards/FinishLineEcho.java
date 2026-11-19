package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class FinishLineEcho extends RaceCard {
    public FinishLineEcho() {
        super("FinishLineEcho", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(16, 3);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.FINISH, magicNumber);
    }

    @Override
    public FinishLineEcho makeCopy() {
        return new FinishLineEcho();
    }
}
