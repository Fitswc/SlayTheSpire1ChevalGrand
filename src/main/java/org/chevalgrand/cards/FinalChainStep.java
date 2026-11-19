package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class FinalChainStep extends RaceCard {
    public FinalChainStep() {
        super("FinalChainStep", CardRarity.RARE, CardType.ATTACK, CardTarget.ENEMY, 2, 2);
        configureDamage(16, 2);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        int hits = previousAttacks() > 0 ? 4 : 2;
        for (int i = 0; i < hits; i++) hit(player, enemy, damage);
    }

    @Override
    public FinalChainStep makeCopy() {
        return new FinalChainStep();
    }
}
