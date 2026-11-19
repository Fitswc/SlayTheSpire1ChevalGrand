package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import org.chevalgrand.ChevalGrandSlayMod;

public final class EnergyReservePower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:ENERGY");

    private int carry;

    public EnergyReservePower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "KeepSomeInReserve");
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer) carry = EnergyPanel.totalCount;
    }

    @Override
    public void onEnergyRecharge() {
        if (carry <= 0) return;
        AbstractPlayer player = (AbstractPlayer) owner;
        if (!player.hasRelic("Ice Cream") && !player.hasPower("Conserve")) addToBot(new GainEnergyAction(carry));
        carry = 0;
    }
}
