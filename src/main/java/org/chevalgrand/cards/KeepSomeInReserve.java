package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.EnergyReservePower;

public final class KeepSomeInReserve extends RaceCard {
    public KeepSomeInReserve() {
        super("KeepSomeInReserve", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new EnergyReservePower(player, 1));
    }

    @Override
    public KeepSomeInReserve makeCopy() {
        return new KeepSomeInReserve();
    }
}
