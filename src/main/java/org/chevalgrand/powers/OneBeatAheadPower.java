package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import org.chevalgrand.ChevalGrandSlayMod;

public final class OneBeatAheadPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:EXTRA_TURN");

    private boolean skippedEnemyTurn;

    public OneBeatAheadPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "OneBeatAhead");
        updateDescription();
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (isPlayer && !skippedEnemyTurn) {
            skippedEnemyTurn = true;
            addToTop(new SkipEnemiesTurnAction());
        }
    }

    @Override
    public void atStartOfTurn() {
        if (skippedEnemyTurn) {
            addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1)));
            addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, 1)));
            removeSelf();
        }
    }

    @Override
    public void stackPower(int extra) {
        // Reapplying does not queue another extra turn.
    }
}
