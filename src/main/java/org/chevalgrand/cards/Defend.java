package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import org.chevalgrand.ChevalGrandSlayMod;

public final class Defend extends StarterCard {
    public static final String ID = ChevalGrandSlayMod.id("Defend");

    public Defend() {
        super(ID, "Defend", 1, 0, 5, 0, 3, CardType.SKILL, CardTarget.SELF);
        tags.add(CardTags.STARTER_DEFEND);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster enemy) {
        gainBlock(player);
    }

    @Override
    public Defend makeCopy() {
        return new Defend();
    }
}
