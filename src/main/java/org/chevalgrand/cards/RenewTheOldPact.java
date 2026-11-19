package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class RenewTheOldPact extends RaceCard {
    public RenewTheOldPact() {
        super("RenewTheOldPact", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.PACT, upgraded ? 2 : 1);
    }

    @Override
    public RenewTheOldPact makeCopy() {
        return new RenewTheOldPact();
    }
}
