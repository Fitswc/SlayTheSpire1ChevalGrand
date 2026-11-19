package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class LongSprint extends RaceCard {
    public LongSprint() {
        super("LongSprint", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY, 2, 4);
        configureDamage(12, 5);
        configureMagic(4, 2);
        selfRetain = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy, damage);
    }

    private int retainedOn = -2;
    @Override public void onRetained() { retainedOn = GameActionManager.turn; }
    @Override public void applyPowers() {
        int original = baseDamage;
        if (retainedOn == GameActionManager.turn - 1) baseDamage += magicNumber;
        super.applyPowers();
        isDamageModified = damage != original;
        baseDamage = original;
    }
    @Override public void calculateCardDamage(AbstractMonster enemy) {
        int original = baseDamage;
        if (retainedOn == GameActionManager.turn - 1) baseDamage += magicNumber;
        super.calculateCardDamage(enemy);
        isDamageModified = damage != original;
        baseDamage = original;
    }

    @Override
    public LongSprint makeCopy() {
        return new LongSprint();
    }
}
