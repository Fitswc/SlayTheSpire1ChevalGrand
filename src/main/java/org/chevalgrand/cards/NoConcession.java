package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.FullBlockCounterPower;

public final class NoConcession extends RaceCard {
    public NoConcession() {
        super("NoConcession", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 1, 4);
        configureBlock(6, 3);
        configureMagic(9, 3);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        apply(new FullBlockCounterPower(player, magicNumber));
    }

    @Override
    public NoConcession makeCopy() {
        return new NoConcession();
    }
}
