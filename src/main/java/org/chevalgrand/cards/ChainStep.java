package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class ChainStep extends RaceCard {
    public ChainStep() {
        super("ChainStep", CardRarity.COMMON, CardType.ATTACK, CardTarget.ENEMY, 1, 6);
        configureDamage(10, 3);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy, damage);
    }

    private int discountedTurn = -1;
    @Override public void applyPowers() {
        super.applyPowers();
        int turn = GameActionManager.turn;
        if (AbstractDungeon.player != null && AbstractDungeon.player.hand.contains(this)
                && turn != discountedTurn && previousAttacks() >= 2) {
            setCostForTurn(costForTurn - 1);
            discountedTurn = turn;
        }
    }

    @Override
    public ChainStep makeCopy() {
        return new ChainStep();
    }
}
