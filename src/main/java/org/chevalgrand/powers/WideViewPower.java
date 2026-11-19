package org.chevalgrand.powers;

import basemod.BaseMod;
import com.megacrit.cardcrawl.core.AbstractCreature;
import java.util.ArrayList;
import java.util.List;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.cards.RaceCard;
import org.chevalgrand.mechanics.Stamina;

public final class WideViewPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:WIDE");

    private static int handLimitBonus;
    private int recovery;

    public WideViewPower(AbstractCreature owner, int amount, int recovery) {
        super(POWER_ID, owner, amount, "WideView");
        this.recovery = recovery;
        updateDescription();
    }

    @Override
    public void updateDescription() {
        description = description(0).replace("{recovery}", Integer.toString(recovery))
                .replace("{count}", Integer.toString(recovery + 1));
    }

    @Override
    public void onInitialApplication() {
        changeHandLimit(amount);
    }

    @Override
    public void stackPower(int extra) {
        changeHandLimit(extra);
        super.stackPower(extra);
    }

    @Override
    public void onRemove() {
        changeHandLimit(-Math.min(amount, handLimitBonus));
    }

    @Override
    public void atStartOfTurn() {
        List<RaceCard> cards = Stamina.restingCards();
        for (RaceCard card : new ArrayList<>(cards.subList(0, Math.min(recovery + 1, cards.size())))) {
            Stamina.moveToDraw(card, recovery);
        }
    }

    void mergeRecovery(WideViewPower incoming) {
        recovery = Math.max(recovery, incoming.recovery);
    }

    static void resetHandLimit() {
        if (handLimitBonus != 0) changeHandLimit(-handLimitBonus);
    }

    private static void changeHandLimit(int delta) {
        handLimitBonus += delta;
        BaseMod.MAX_HAND_SIZE += delta;
    }
}
