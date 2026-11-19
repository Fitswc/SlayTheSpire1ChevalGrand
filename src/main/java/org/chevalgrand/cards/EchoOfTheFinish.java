package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.OverflowEchoPower;

public final class EchoOfTheFinish extends RaceCard {
    public EchoOfTheFinish() {
        super("EchoOfTheFinish", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new OverflowEchoPower(player, 999));
    }

    @Override
    public EchoOfTheFinish makeCopy() {
        return new EchoOfTheFinish();
    }
}
