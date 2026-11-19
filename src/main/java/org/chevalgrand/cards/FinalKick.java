package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

public final class FinalKick extends RaceCard {
    public FinalKick() {
        super("FinalKick", CardRarity.RARE, CardType.ATTACK, CardTarget.ALL_ENEMY, 0, 0);
        configureDamage(20171126, 0);
        selfRetain = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        if (ready(player)) addToBot(new DamageAllEnemiesAction(player, multiDamage, damageTypeForTurn, SLASH));
    }

    private boolean ready(AbstractPlayer player) {
        return EnergyPanel.totalCount == 0 && !Stamina.REST.isEmpty()
                && player.drawPile.group.stream().noneMatch(c -> Stamina.has(c, 0))
                && player.discardPile.group.stream().noneMatch(c -> Stamina.has(c, 0));
    }
    @Override public boolean canUse(AbstractPlayer player, AbstractMonster enemy) {
        return ready(player) && super.canUse(player, enemy);
    }
    @Override public void triggerOnGlowCheck() {
        glowColor = AbstractDungeon.player != null && ready(AbstractDungeon.player)
                ? GOLD_BORDER_GLOW_COLOR.cpy() : BLUE_BORDER_GLOW_COLOR.cpy();
    }

    @Override
    public FinalKick makeCopy() {
        return new FinalKick();
    }
}
