package org.chevalgrand.cards;

import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoCompendium;

@NoCompendium
public final class ChaseTheWindDiscardPile extends ChasePileChoice {
    public ChaseTheWindDiscardPile() {
        super("ChaseTheWindDiscardPile");
    }

    @Override
    public ChaseTheWindDiscardPile makeCopy() {
        return new ChaseTheWindDiscardPile();
    }
}
