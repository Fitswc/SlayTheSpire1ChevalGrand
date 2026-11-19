package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class FlowingAssault extends RaceCard {
    public FlowingAssault() {
        super("FlowingAssault", CardRarity.UNCOMMON, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new ApplyPowerAction(player, player, new RacePower(RacePower.Effect.FlowingAssault, player, magicNumber)));
    }

    @Override
    public FlowingAssault makeCopy() {
        return new FlowingAssault();
    }
}
