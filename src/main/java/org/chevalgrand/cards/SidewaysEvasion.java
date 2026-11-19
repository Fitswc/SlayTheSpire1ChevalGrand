package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class SidewaysEvasion extends RaceCard {
    public SidewaysEvasion() {
        super("SidewaysEvasion", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 6);
        configureBlock(10, 3);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        if (player.hand.group.stream().noneMatch(card -> card != this && card instanceof RaceCard
                && ((RaceCard) card).remaining > 0)) draw(player, 2);
    }

    @Override
    public SidewaysEvasion makeCopy() {
        return new SidewaysEvasion();
    }
}
