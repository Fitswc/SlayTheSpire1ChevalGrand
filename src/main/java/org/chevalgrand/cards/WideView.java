package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class WideView extends RaceCard {
    public WideView() {
        super("WideView", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        RaceAbility power = new RaceAbility(RaceAbility.Effect.WIDE, player, 2);
        power.recovery = magicNumber;
        addToBot(new ApplyPowerAction(player, player, power));
    }

    @Override
    public WideView makeCopy() {
        return new WideView();
    }
}
