package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class ReplanTheRoute extends RaceCard {
    public ReplanTheRoute() {
        super("ReplanTheRoute", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 1, 4);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardSelection.shuffleDiscard(3));
        draw(player, 1);
    }

    @Override
    public ReplanTheRoute makeCopy() {
        return new ReplanTheRoute();
    }
}
