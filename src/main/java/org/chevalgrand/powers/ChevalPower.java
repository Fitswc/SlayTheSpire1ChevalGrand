package org.chevalgrand.powers;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import org.chevalgrand.cards.RaceCard;

abstract class ChevalPower extends AbstractPower {
    private final PowerStrings strings;

    ChevalPower(String id, AbstractCreature owner, int amount, String art) {
        this(id, owner, amount, art, art);
    }

    ChevalPower(String id, AbstractCreature owner, int amount, String art, String largeArt) {
        ID = id;
        this.owner = owner;
        this.amount = amount;
        strings = CardCrawlGame.languagePack.getPowerStrings(id);
        name = strings.NAME;
        type = PowerType.BUFF;
        region48 = RacePowerArt.region(art);
        region128 = RacePowerArt.region(largeArt);
    }

    @Override
    public void updateDescription() {
        description = description(0);
    }

    final String description(int variant) {
        return strings.DESCRIPTIONS[variant].replace("{amount}", Integer.toString(amount));
    }

    final void removeSelf() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, ID));
    }

    final void gainBlock(int value) {
        addToBot(new GainBlockAction(owner, owner, value));
    }

    final void damageAllEnemies(int damage) {
        addToBot(new DamageAllEnemiesAction(owner, DamageInfo.createDamageMatrix(damage, true),
                DamageInfo.DamageType.THORNS, RaceCard.SLASH));
    }

    static boolean isAttackDamage(DamageInfo.DamageType type, AbstractCard card) {
        return type == DamageInfo.DamageType.NORMAL && card != null && card.type == AbstractCard.CardType.ATTACK;
    }
}
