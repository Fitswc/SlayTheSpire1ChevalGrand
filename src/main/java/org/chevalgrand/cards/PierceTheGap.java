package org.chevalgrand.cards;

import com.evacipated.cardcrawl.mod.stslib.damagemods.AbstractDamageModifier;
import com.evacipated.cardcrawl.mod.stslib.damagemods.DamageModifierManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class PierceTheGap extends RaceCard {
    public PierceTheGap() {
        super("PierceTheGap", CardRarity.COMMON, CardType.ATTACK, CardTarget.ENEMY, 1, 6);
        configureDamage(10, 3);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        DamageInfo info = new DamageInfo(player, damage, damageTypeForTurn);
        DamageModifierManager.bindDamageMods(info, java.util.Collections.singletonList(new BypassBlock()));
        addToBot(new DamageAction(enemy, info, SLASH));
    }

    @Override
    public PierceTheGap makeCopy() {
        return new PierceTheGap();
    }

    private static final class BypassBlock extends AbstractDamageModifier {
        @Override
        public boolean ignoresBlock(AbstractCreature target) {
            return true;
        }

        @Override
        public AbstractDamageModifier makeCopy() {
            return new BypassBlock();
        }
    }
}
