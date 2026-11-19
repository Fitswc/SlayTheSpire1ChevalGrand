package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.CardSelection;

public final class ReturnMarker extends RaceCard {
    public ReturnMarker() {
        super("ReturnMarker", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 0, 4);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardSelection.combatDiscount(this, magicNumber));
        draw(player, 1);
    }

    @Override
    public ReturnMarker makeCopy() {
        return new ReturnMarker();
    }
}
