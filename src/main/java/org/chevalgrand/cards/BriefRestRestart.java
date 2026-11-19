package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.CardSelection;

public final class BriefRestRestart extends RaceCard {
    public BriefRestRestart() {
        super("BriefRestRestart", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 6);
        configureBlock(4, 3);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        addToBot(CardSelection.fromRest(2, true, true, false));
    }

    @Override
    public BriefRestRestart makeCopy() {
        return new BriefRestRestart();
    }
}
