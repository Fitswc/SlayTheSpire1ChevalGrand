package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.OldPactPower;

public final class RenewTheOldPact extends RaceCard {
    public RenewTheOldPact() {
        super("RenewTheOldPact", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new OldPactPower(player, upgraded ? 2 : 1));
    }

    @Override
    public RenewTheOldPact makeCopy() {
        return new RenewTheOldPact();
    }
}
