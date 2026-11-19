package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

abstract class ChasePileChoice extends RaceCard {
    ChasePileChoice(String name) {
        super(name, "ChaseTheWind", CardRarity.SPECIAL, CardType.SKILL, CardTarget.SELF, 0, 0);
        initializeDescription();
    }

    @Override
    public final boolean canUse(AbstractPlayer player, AbstractMonster enemy) {
        return false;
    }

    @Override
    final void onPlay(AbstractPlayer player, AbstractMonster enemy) {
    }
}
