package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class PrepareEarly extends RaceCard {
    public PrepareEarly() {
        super("PrepareEarly", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 0, 0);
        exhaust = true;
        initializeDescription();
    }

    @Override
    public boolean canUpgrade() {
        return !upgraded;
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardSelection.upgradeDrawPile());
    }

    @Override
    public PrepareEarly makeCopy() {
        return new PrepareEarly();
    }
}
