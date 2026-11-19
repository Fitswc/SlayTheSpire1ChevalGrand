package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.NextTurnBlockPower;

public final class PrepareForNextStep extends RaceCard {
    public PrepareForNextStep() {
        super("PrepareForNextStep", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 2, 0);
        configureBlock(14, 3);
        configureMagic(16, 5);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        addToBot(new ApplyPowerAction(player, player, new NextTurnBlockPower(player, magicNumber + block - baseBlock)));
    }

    @Override
    public PrepareForNextStep makeCopy() {
        return new PrepareForNextStep();
    }
}
