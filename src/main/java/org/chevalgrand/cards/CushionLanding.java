package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.mechanics.Stamina;

public final class CushionLanding extends RaceCard {
    public CushionLanding() {
        super("CushionLanding", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 6);
        configureBlock(8, 3);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        if (Stamina.restedThisTurn()) draw(player, 1);
    }

    @Override
    public CushionLanding makeCopy() {
        return new CushionLanding();
    }
}
