package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.ArrayList;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.cards.RaceCard;
import org.chevalgrand.mechanics.CardMoves;
import org.chevalgrand.mechanics.Stamina;

public final class DomainPower extends ChevalPower {
    public static final String POWER_ID = ChevalGrandSlayMod.id("Power:DOMAIN");

    public DomainPower(AbstractCreature owner, int amount) {
        super(POWER_ID, owner, amount, "Domain");
        updateDescription();
    }

    @Override
    public void onInitialApplication() {
        addToBot(new GainEnergyAction(4));
        addToBot(CardMoves.run(() -> {
            for (RaceCard card : new ArrayList<>(Stamina.restingCards())) {
                if (!Stamina.returnToHand(card, true, false)) Stamina.moveToDraw(card, card.maxStamina());
            }
        }));
    }

    @Override
    public void onEnergyRecharge() {
        addToBot(new GainEnergyAction(4));
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) return;
        if (amount > 1) {
            addToBot(new ReducePowerAction(owner, owner, ID, 1));
        } else if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            addToBot(new DamageAction(owner, new DamageInfo(owner, owner.currentHealth, DamageInfo.DamageType.HP_LOSS),
                    AbstractGameAction.AttackEffect.NONE));
        }
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        return isAttackDamage(type, card) ? damage * 1.8f : damage;
    }
}
