package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class TurnTheOldPage extends RaceCard {
    public TurnTheOldPage() {
        super("TurnTheOldPage", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 0, 0);
        configureMagic(2, 1);
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardMoves.hand(this, c -> Stamina.has(c, 2) && ((RaceCard)c).deckOrigin != null, c -> {
            RaceCard card = (RaceCard)c;
            card.setRemaining(card.remaining - 2);
            if (card.remaining == 0) player.hand.moveToDiscardPile(card);
            AbstractDungeon.actionManager.addToTop(new DrawCardAction(player, magicNumber));
        }));
    }

    @Override
    public TurnTheOldPage makeCopy() {
        return new TurnTheOldPage();
    }
}
