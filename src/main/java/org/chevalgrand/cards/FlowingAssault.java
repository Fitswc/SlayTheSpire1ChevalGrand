package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.FlowingAssaultPower;

public final class FlowingAssault extends RaceCard {
    public FlowingAssault() {
        super("FlowingAssault", CardRarity.UNCOMMON, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new FlowingAssaultPower(player, magicNumber));
    }

    @Override
    public FlowingAssault makeCopy() {
        return new FlowingAssault();
    }
}
