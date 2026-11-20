package org.chevalgrand.cards;

import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import java.util.List;
import org.chevalgrand.enchantments.ReserveStepEnchantment;
import org.chevalgrand.mechanics.CardMoves;
import org.chevalgrand.mechanics.Stamina;

public final class ReserveAStep extends RaceCard {
    public ReserveAStep() {
        super("ReserveAStep", CardRarity.COMMON, CardType.SKILL, CardTarget.SELF, 1, 6);
        configureBlock(5, 2);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player, block);
        addToBot(CardMoves.hand(this, c -> Stamina.has(c, 1), ReserveStepEnchantment::apply));
    }

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        List<TooltipInfo> tips = super.getCustomTooltips();
        if (!ReserveStepEnchantment.preservesStamina(this)) tips.add(ReserveStepEnchantment.tooltip());
        return tips;
    }

    @Override
    public ReserveAStep makeCopy() {
        return new ReserveAStep();
    }
}
