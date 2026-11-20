package org.chevalgrand.cards;

import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoCompendium;

@NoCompendium
public final class ChaseTheWindRestPile extends ChasePileChoice {
    public ChaseTheWindRestPile() {
        super("ChaseTheWindRestPile");
    }

    @Override
    public ChaseTheWindRestPile makeCopy() {
        return new ChaseTheWindRestPile();
    }
}
