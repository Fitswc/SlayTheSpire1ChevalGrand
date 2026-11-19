package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class CorrectTheFormation extends RaceCard {
    public CorrectTheFormation() {
        super("CorrectTheFormation", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 0, 0);
        configureMagic(4, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardSelection.inspectDraw(magicNumber, 2, upgraded, false));
    }

    @Override
    public CorrectTheFormation makeCopy() {
        return new CorrectTheFormation();
    }
}
