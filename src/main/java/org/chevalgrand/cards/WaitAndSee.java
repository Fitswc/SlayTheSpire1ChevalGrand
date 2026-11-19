package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class WaitAndSee extends RaceCard {
    public WaitAndSee() {
        super("WaitAndSee", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 1, 0);
        configureBlock(12, 2);
        configureMagic(2, 0);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        addToBot(new DrawCardAction(magicNumber, new AbstractGameAction() {
            @Override public void update() {
                addToTop(CardSelection.discardDrawn(DrawCardAction.drawnCards));
                isDone = true;
            }
        }));
    }

    @Override
    public WaitAndSee makeCopy() {
        return new WaitAndSee();
    }
}
