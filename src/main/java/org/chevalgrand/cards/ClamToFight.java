package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.VigorPower;

public final class ClamToFight extends RaceCard {
    public ClamToFight() {
        super("ClamToFight", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 0);
        configureBlock(8, 3);
        configureMagic(4, 1);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        apply(new VigorPower(player, magicNumber));
    }

    @Override
    public ClamToFight makeCopy() {
        return new ClamToFight();
    }
}
