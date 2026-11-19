package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.Stamina;

public final class GuardTheFinish extends RaceCard {
    public GuardTheFinish() {
        super("GuardTheFinish", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 1, 4);
        configureBlock(12, 4);
        configureMagic(4, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        gainBlock(player, magicNumber * Stamina.restingCards().size());
    }

    @Override
    public GuardTheFinish makeCopy() {
        return new GuardTheFinish();
    }
}
