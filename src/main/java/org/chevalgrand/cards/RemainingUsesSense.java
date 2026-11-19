package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class RemainingUsesSense extends RaceCard {
    public RemainingUsesSense() {
        super("RemainingUsesSense", CardRarity.UNCOMMON, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(14, 2);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new ApplyPowerAction(player, player, new RacePower(RacePower.Effect.RemainingUsesSense, player, magicNumber)));
    }

    @Override
    public RemainingUsesSense makeCopy() {
        return new RemainingUsesSense();
    }
}
