package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.EnemyAttackLossCapPower;

public final class MeasuredAdvanceAndRetreat extends RaceCard {
    public MeasuredAdvanceAndRetreat() {
        super("MeasuredAdvanceAndRetreat", CardRarity.RARE, CardType.SKILL, CardTarget.SELF, 2, 2);
        configureMagic(12, -4);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new EnemyAttackLossCapPower(player, magicNumber));
    }

    @Override
    public MeasuredAdvanceAndRetreat makeCopy() {
        return new MeasuredAdvanceAndRetreat();
    }
}
