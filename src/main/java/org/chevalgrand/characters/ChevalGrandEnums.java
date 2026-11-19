package org.chevalgrand.characters;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.helpers.CardLibrary.LibraryType;
import org.chevalgrand.ChevalGrandSlayMod;

public final class ChevalGrandEnums {
    @SpireEnum(name = "CHEVAL_GRAND")
    public static PlayerClass PLAYER_CLASS;
    @SpireEnum(name = "CHEVAL_GRAND_TEAL")
    public static CardColor CARD_COLOR;
    @SpireEnum(name = "CHEVAL_GRAND_TEAL")
    public static LibraryType LIBRARY_TYPE;

    static final Color THEME = new Color(0.4f, 0.76f, 0.76f, 1f);
    static final String ENERGY = ChevalGrandSlayMod.IMAGES + "characters/energy/";
    private static final String CARD_ART = ChevalGrandSlayMod.IMAGES + "cards/frames/";

    private ChevalGrandEnums() {
    }

    public static void registerColor() {
        BaseMod.addColor(CARD_COLOR, THEME,
                CARD_ART + "attack.png", CARD_ART + "skill.png", CARD_ART + "power.png", ENERGY + "cost.png",
                CARD_ART + "attack-large.png", CARD_ART + "skill-large.png", CARD_ART + "power-large.png",
                ENERGY + "cost-large.png", ENERGY + "text.png");
    }
}
