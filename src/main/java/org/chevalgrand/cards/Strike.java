package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import org.chevalgrand.ChevalGrandSlayMod;

public final class Strike extends StarterCard {
    public static final String ID = ChevalGrandSlayMod.id("Strike");

    public Strike() {
        super(ID, "Strike", 1, 6, 0, 3, 0, CardType.ATTACK, CardTarget.ENEMY);
        tags.add(CardTags.STARTER_STRIKE);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy);
    }

    @Override
    public Strike makeCopy() {
        return new Strike();
    }
}
