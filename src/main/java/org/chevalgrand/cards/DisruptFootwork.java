package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class DisruptFootwork extends RaceCard {
    public DisruptFootwork() {
        super("DisruptFootwork", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ALL_ENEMY, 0, 4);
        configureDamage(8, 2);
        configureMagic(2, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) vulnerable(player, monster);
        }
        addToBot(new AbstractGameAction() {
            @Override public void update() {
                calculateCardDamage(null);
                addToTop(new DamageAllEnemiesAction(player, multiDamage, damageTypeForTurn, SLASH));
                isDone = true;
            }
        });
    }

    @Override
    public DisruptFootwork makeCopy() {
        return new DisruptFootwork();
    }
}
