package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class EarthshakingStep extends RaceCard {
    public EarthshakingStep() {
        super("EarthshakingStep", CardRarity.RARE, CardType.ATTACK, CardTarget.ALL_ENEMY, 3, 2);
        configureDamage(28, 4);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new DamageAllEnemiesAction(player, multiDamage, damageTypeForTurn, SLASH));
    }

    @Override
    public EarthshakingStep makeCopy() {
        return new EarthshakingStep();
    }
}
