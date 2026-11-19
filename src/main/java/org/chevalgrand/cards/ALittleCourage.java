package org.chevalgrand.cards;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import org.chevalgrand.ChevalGrandSlayMod;

public final class ALittleCourage extends StarterCard {
    public static final String ID = ChevalGrandSlayMod.id("ALittleCourage");

    public ALittleCourage() {
        super(ID, "ALittleCourage", 2, 10, 8, 3, 2, CardType.ATTACK, CardTarget.ENEMY);
        tags.add(CardTags.STRIKE);
    }

    @Override
    public void use(AbstractPlayer player, AbstractMonster enemy) {
        hit(player, enemy);
        gainBlock(player);
    }

    @Override
    public ALittleCourage makeCopy() {
        return new ALittleCourage();
    }
}
