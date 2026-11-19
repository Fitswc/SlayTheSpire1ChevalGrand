package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.TurnTogetherPower;

public final class TurnTogether extends RaceCard {
    public TurnTogether() {
        super("TurnTogether", CardRarity.UNCOMMON, CardType.POWER, CardTarget.SELF, 1, 0);
        configureMagic(0, 10);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new TurnTogetherPower(player, upgraded ? 10 : 0));
    }

    @Override
    public TurnTogether makeCopy() {
        return new TurnTogether();
    }
}
