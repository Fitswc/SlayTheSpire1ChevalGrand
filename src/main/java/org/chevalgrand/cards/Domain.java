package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class Domain extends RaceCard {
    public Domain() {
        super("Domain", CardRarity.RARE, CardType.SKILL, CardTarget.SELF, 0, 0);
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        if (!player.hasPower(RaceAbility.Effect.DOMAIN.id)) apply(player, RaceAbility.Effect.DOMAIN, 2);
    }

    @Override public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return !p.hasPower(RaceAbility.Effect.DOMAIN.id) && super.canUse(p, m);
    }

    @Override
    public Domain makeCopy() {
        return new Domain();
    }
}
