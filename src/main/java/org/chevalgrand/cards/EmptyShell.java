package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class EmptyShell extends RaceCard {
    public EmptyShell() {
        super("EmptyShell", CardRarity.SPECIAL, CardType.SKILL, CardTarget.SELF, 0, 0);
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
    }

    @Override
    public EmptyShell makeCopy() {
        return new EmptyShell();
    }
}
