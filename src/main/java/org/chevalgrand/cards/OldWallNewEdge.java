package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInDrawPileAction;
import com.megacrit.cardcrawl.cards.status.Dazed;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class OldWallNewEdge extends RaceCard {
    public OldWallNewEdge() {
        super("OldWallNewEdge", CardRarity.UNCOMMON, CardType.ATTACK, CardTarget.ENEMY, 2, 4);
        configureDamage(12, 0);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new MakeTempCardInDrawPileAction(new Dazed(), 2, true, true));
        hit(player, enemy, damage);
        hit(player, enemy, damage);
    }

    @Override
    public OldWallNewEdge makeCopy() {
        return new OldWallNewEdge();
    }
}
