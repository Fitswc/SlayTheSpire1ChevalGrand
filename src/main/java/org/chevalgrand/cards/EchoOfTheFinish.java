package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class EchoOfTheFinish extends RaceCard {
    public EchoOfTheFinish() {
        super("EchoOfTheFinish", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(player, RaceAbility.Effect.OVERFLOW, 999);
    }

    @Override
    public EchoOfTheFinish makeCopy() {
        return new EchoOfTheFinish();
    }
}
