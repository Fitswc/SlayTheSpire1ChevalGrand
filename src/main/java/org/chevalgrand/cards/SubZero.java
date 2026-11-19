package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.PlatedArmorPower;

public final class SubZero extends RaceCard {
    public SubZero() {
        super("SubZero", CardRarity.RARE, CardType.POWER, CardTarget.SELF, 2, 0);
        configureMagic(16, 2);
        costUpgrade = -1;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(CardMoves.hand(this, c -> true, CardMoves::discard));
        addToBot(new ApplyPowerAction(player, player, new PlatedArmorPower(player, magicNumber)));
        addToBot(CardMoves.run(() -> new java.util.ArrayList<>(Stamina.REST)
                .forEach(c -> Stamina.moveToDraw(c, upgraded ? 2 : 1))));
    }

    @Override
    public SubZero makeCopy() {
        return new SubZero();
    }
}
