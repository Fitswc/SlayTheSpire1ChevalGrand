package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class ForgeAhead extends RaceCard {
    public ForgeAhead() {
        super("ForgeAhead", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.FORGE, 1);
    }

    @Override
    public ForgeAhead makeCopy() {
        return new ForgeAhead();
    }
}
