package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class BreakTheOpening extends RaceCard {
    public BreakTheOpening() {
        super("BreakTheOpening", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY, 1, 4);
        configureDamage(10, 3);
        configureMagic(2, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        vulnerable(player, enemy);
        addToBot(new AbstractGameAction() {
            @Override public void update() {
                calculateCardDamage(enemy);
                addToTop(new DamageAction(enemy, new DamageInfo(player, damage, damageTypeForTurn), SLASH));
                isDone = true;
            }
        });
    }

    @Override
    public BreakTheOpening makeCopy() {
        return new BreakTheOpening();
    }
}
