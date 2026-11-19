package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.Stamina;

public final class CountTheCost extends RaceCard {
    public CountTheCost() {
        super("CountTheCost", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 0, 4);
        configureMagic(2, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        draw(player, magicNumber);
        if (Stamina.restedThisTurn()) addToBot(new GainEnergyAction(2));
    }

    @Override
    public CountTheCost makeCopy() {
        return new CountTheCost();
    }
}
