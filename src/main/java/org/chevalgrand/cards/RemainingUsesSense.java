package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.RemainingUsesSensePower;

public final class RemainingUsesSense extends RaceCard {
    public RemainingUsesSense() {
        super("RemainingUsesSense", CardRarity.UNCOMMON, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(14, 2);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new RemainingUsesSensePower(player, magicNumber));
    }

    @Override
    public RemainingUsesSense makeCopy() {
        return new RemainingUsesSense();
    }
}
