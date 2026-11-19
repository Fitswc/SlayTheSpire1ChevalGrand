package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.WideViewPower;

public final class WideView extends RaceCard {
    public WideView() {
        super("WideView", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(1, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new WideViewPower(player, 2, magicNumber));
    }

    @Override
    public WideView makeCopy() {
        return new WideView();
    }
}
