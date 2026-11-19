package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class FateEcho extends RaceCard {
    public FateEcho() {
        super("FateEcho", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 2, 4);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        Stamina.restoreAll();
    }

    @Override
    public FateEcho makeCopy() {
        return new FateEcho();
    }
}
