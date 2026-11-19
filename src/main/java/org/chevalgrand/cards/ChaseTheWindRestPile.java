package org.chevalgrand.cards;

public final class ChaseTheWindRestPile extends ChasePileChoice {
    public ChaseTheWindRestPile() {
        super("ChaseTheWindRestPile");
    }

    @Override
    public ChaseTheWindRestPile makeCopy() {
        return new ChaseTheWindRestPile();
    }
}
