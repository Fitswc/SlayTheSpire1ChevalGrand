package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.OneBeatAheadPower;

public final class OneBeatAhead extends RaceCard {
    public OneBeatAhead() {
        super("OneBeatAhead", CardRarity.SPECIAL, CardType.SKILL, CardTarget.SELF, 3, 0);
        costUpgrade = -1;
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new OneBeatAheadPower(player, 1));
    }

    @Override
    public OneBeatAhead makeCopy() {
        return new OneBeatAhead();
    }
}
