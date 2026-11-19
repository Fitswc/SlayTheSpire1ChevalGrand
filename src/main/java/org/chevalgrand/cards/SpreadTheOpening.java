package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class SpreadTheOpening extends RaceCard {
    public SpreadTheOpening() {
        super("SpreadTheOpening", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.ALL_ENEMY, 0, 0);
        configureMagic(2, 2);
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        for (AbstractMonster monster : AbstractDungeon.getMonsters().monsters) {
            if (!monster.isDeadOrEscaped()) vulnerable(player, monster);
        }
    }

    @Override
    public SpreadTheOpening makeCopy() {
        return new SpreadTheOpening();
    }
}
