package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class AimForTheFinish extends RaceCard {
    public AimForTheFinish() {
        super("AimForTheFinish", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 0, 4);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardMoves.fetch(magicNumber));
    }

    @Override
    public AimForTheFinish makeCopy() {
        return new AimForTheFinish();
    }
}
