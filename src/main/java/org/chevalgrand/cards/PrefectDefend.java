package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.PerfectDefendPower;

public final class PrefectDefend extends RaceCard {
    public PrefectDefend() {
        super("PrefectDefend", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(16, 4);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new PerfectDefendPower(player, magicNumber));
    }

    @Override
    public PrefectDefend makeCopy() {
        return new PrefectDefend();
    }
}
