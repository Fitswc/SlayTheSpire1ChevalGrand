package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class DrawFinishLine extends RaceCard {
    public DrawFinishLine() {
        super("DrawFinishLine", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, -1, 4);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        int energy = energyOnUse;
        if (player.hasRelic("Chemical X")) {
            player.getRelic("Chemical X").flash();
            energy += 2;
        }
        if (energy > 0) addToBot(CardSelection.recoverHalf(energy));
        draw(player, magicNumber);
        if (!freeToPlayOnce) player.energy.use(energyOnUse);
    }

    @Override
    public DrawFinishLine makeCopy() {
        return new DrawFinishLine();
    }
}
