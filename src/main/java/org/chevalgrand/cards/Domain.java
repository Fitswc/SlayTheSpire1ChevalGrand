package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.powers.DomainPower;

public final class Domain extends RaceCard {
    public Domain() {
        super("Domain", CardRarity.RARE, CardType.SKILL, CardTarget.SELF, 0, 0);
        exhaust = true;
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        if (!player.hasPower(DomainPower.POWER_ID)) apply(new DomainPower(player, 2));
    }

    @Override public boolean canUse(AbstractPlayer p, AbstractMonster m) {
        return !p.hasPower(DomainPower.POWER_ID) && super.canUse(p, m);
    }

    @Override
    public Domain makeCopy() {
        return new Domain();
    }
}
