package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class ReorderNotes extends RaceCard {
    public ReorderNotes() {
        super("ReorderNotes", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 0, 0);
        configureMagic(5, 1);
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        draw(player, Math.max(0, magicNumber - (player.hand.size() - (player.hand.contains(this) ? 1 : 0))));
    }

    @Override
    public ReorderNotes makeCopy() {
        return new ReorderNotes();
    }
}
