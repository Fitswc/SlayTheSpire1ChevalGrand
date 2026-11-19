package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class RepurposeBulwark extends RaceCard {
    public RepurposeBulwark() {
        super("RepurposeBulwark", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.ENEMY, 1, 4);
        configureMagic(20, 4);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        int stolen = Math.min(enemy.currentBlock, magicNumber);
        if (stolen > 0) {
            addToBot(new LoseBlockAction(enemy, player, stolen));
            gainBlock(player, stolen);
        } else {
            addToBot(new DamageAction(enemy, new DamageInfo(player, 12, DamageInfo.DamageType.HP_LOSS), SLASH));
        }
    }

    @Override
    public RepurposeBulwark makeCopy() {
        return new RepurposeBulwark();
    }
}
