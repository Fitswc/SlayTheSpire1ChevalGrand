package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class Reschedule extends RaceCard {
    public Reschedule() {
        super("Reschedule", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 0, 0);
        configureMagic(3, 1);
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new AbstractGameAction() {
            @Override public void update() {
                player.drawPile.group.addAll(player.discardPile.group);
                player.discardPile.clear();
                player.drawPile.shuffle(AbstractDungeon.shuffleRng);
                isDone = true;
            }
        });
        draw(player, magicNumber);
    }

    @Override
    public Reschedule makeCopy() {
        return new Reschedule();
    }
}
