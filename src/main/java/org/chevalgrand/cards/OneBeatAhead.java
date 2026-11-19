package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class OneBeatAhead extends RaceCard {
    public OneBeatAhead() {
        super("OneBeatAhead", CardRarity.SPECIAL, CardType.SKILL, CardTarget.SELF, 3, 0);
        costUpgrade = -1;
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.EXTRA_TURN, 1);
    }

    @Override
    public OneBeatAhead makeCopy() {
        return new OneBeatAhead();
    }
}
