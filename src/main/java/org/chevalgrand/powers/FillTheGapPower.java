package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.cards.RaceCard;

public final class FillTheGapPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:FILL");

    private int usedTurn = -1;

    public FillTheGapPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "FillTheGap");
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = description(amount >= 2 ? 1 : 0);
    }

    void onRestExit(RaceCard card) {
        int turn = GameActionManager.turn;
        if (usedTurn == turn) return;
        usedTurn = turn;
        card.gainBonusStamina();
        if (amount >= 2) addToBot(new GainEnergyAction(1));
    }
}
