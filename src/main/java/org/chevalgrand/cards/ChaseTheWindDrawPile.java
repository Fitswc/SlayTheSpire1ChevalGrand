package org.chevalgrand.cards;

import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoCompendium;

@NoCompendium
public final class ChaseTheWindDrawPile extends ChasePileChoice {
    public ChaseTheWindDrawPile() {
        super("ChaseTheWindDrawPile");
    }

    @Override
    public ChaseTheWindDrawPile makeCopy() {
        return new ChaseTheWindDrawPile();
    }
}
