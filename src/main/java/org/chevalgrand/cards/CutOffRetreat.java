package org.chevalgrand.cards;

import com.evacipated.cardcrawl.mod.stslib.actions.common.StunMonsterAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class CutOffRetreat extends RaceCard {
    public CutOffRetreat() {
        super("CutOffRetreat", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY, 1, 4);
        configureDamage(12, 3);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy, damage);
        addToBot(new AbstractGameAction() {
            @Override public void update() {
                if (!enemy.isDeadOrEscaped()) addToTop(new StunMonsterAction(enemy, player));
                isDone = true;
            }
        });
    }

    @Override
    public CutOffRetreat makeCopy() {
        return new CutOffRetreat();
    }
}
