package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class NeverStepBehind extends RaceCard {
    public NeverStepBehind() {
        super("NeverStepBehind", CardRarity.RARE, CardType.SKILL, CardTarget.ALL_ENEMY, 2, 2);
        configureBlock(24, 6);
        configureMagic(4, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        for (AbstractMonster target : AbstractDungeon.getMonsters().monsters) {
            if (!target.isDeadOrEscaped()) vulnerable(player, target);
        }
    }

    @Override
    public NeverStepBehind makeCopy() {
        return new NeverStepBehind();
    }
}
