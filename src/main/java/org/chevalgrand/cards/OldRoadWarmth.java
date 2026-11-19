package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.OldRoadWarmthPower;

public final class OldRoadWarmth extends RaceCard {
    public OldRoadWarmth() {
        super("OldRoadWarmth", CardRarity.UNCOMMON, CardType.POWER, CardTarget.SELF, 1, 0);
        configureMagic(12, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        apply(new OldRoadWarmthPower(player, magicNumber));
    }

    @Override
    public OldRoadWarmth makeCopy() {
        return new OldRoadWarmth();
    }
}
