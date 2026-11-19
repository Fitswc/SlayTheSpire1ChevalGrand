package org.chevalgrand.cards;

public final class ChaseTheWindDiscardPile extends ChasePileChoice {
    public ChaseTheWindDiscardPile() {
        super("ChaseTheWindDiscardPile");
    }

    @Override
    public ChaseTheWindDiscardPile makeCopy() {
        return new ChaseTheWindDiscardPile();
    }
}
