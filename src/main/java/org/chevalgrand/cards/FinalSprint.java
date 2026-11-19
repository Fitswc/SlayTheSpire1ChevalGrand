package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.Stamina;

public final class FinalSprint extends RaceCard {
    public FinalSprint() {
        super("FinalSprint", CardRarity.RARE, CardType.ATTACK, CardTarget.ALL_ENEMY, 2, 2);
        configureDamage(10, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        int repeats = Stamina.restingCards().size() + 1;
        for (int i = 0; i < repeats; i++) addToBot(new DamageAllEnemiesAction(player, multiDamage, damageTypeForTurn, SLASH));
    }

    @Override
    public FinalSprint makeCopy() {
        return new FinalSprint();
    }
}
