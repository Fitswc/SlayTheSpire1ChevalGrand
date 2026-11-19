package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class StepSteady extends RaceCard {
    public StepSteady() {
        super("StepSteady", CardRarity.COMMON, CardType.ATTACK, CardTarget.ALL_ENEMY, 1, 0);
        configureDamage(0, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new DamageAllEnemiesAction(player,
                DamageInfo.createDamageMatrix(player.currentBlock, true), DamageInfo.DamageType.NORMAL, SLASH));
    }

    @Override
    public StepSteady makeCopy() {
        return new StepSteady();
    }
}
