package org.chevalgrand.cards;

public final class ChaseTheWindDrawPile extends ChasePileChoice {
    public ChaseTheWindDrawPile() {
        super("ChaseTheWindDrawPile");
    }

    @Override
    public ChaseTheWindDrawPile makeCopy() {
        return new ChaseTheWindDrawPile();
    }
}
