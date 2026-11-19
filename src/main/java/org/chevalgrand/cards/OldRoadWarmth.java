package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class OldRoadWarmth extends RaceCard {
    public OldRoadWarmth() {
        super("OldRoadWarmth", CardRarity.UNCOMMON, CardType.POWER, CardTarget.SELF, 1, 0);
        configureMagic(12, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new ApplyPowerAction(player, player, new RacePower(RacePower.Effect.OldRoadWarmth, player, magicNumber)));
    }

    @Override
    public OldRoadWarmth makeCopy() {
        return new OldRoadWarmth();
    }
}
