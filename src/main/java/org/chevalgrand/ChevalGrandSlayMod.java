package org.chevalgrand;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chevalgrand.cards.CardRegistry;

@SpireInitializer
public final class ChevalGrandSlayMod implements EditCardsSubscriber, EditCharactersSubscriber,
        EditRelicsSubscriber, EditStringsSubscriber, PostInitializeSubscriber {
    static final String ID = "ChevalGrandSlay";
    public static final String IMAGES = ID + "/images/";
    private static final Logger LOGGER = LogManager.getLogger(ChevalGrandSlayMod.class);

    private ChevalGrandSlayMod() {
    }

    public static void initialize() {
        ChevalGrandEnums.registerColor();
        CardRegistry.registerSaveField();
        BaseMod.subscribe(new ChevalGrandSlayMod());
    }

    public static String id(String name) {
        return ID + ":" + name;
    }

    @Override
    public void receiveEditStrings() {
        String locale = Settings.language == Settings.GameLanguage.ZHS ? "zhs" : "eng";
        String path = ID + "/localization/" + locale + "/";
        BaseMod.loadCustomStringsFile(CharacterStrings.class, path + "characters.json");
        BaseMod.loadCustomStringsFile(CardStrings.class, path + "cards.json");
        BaseMod.loadCustomStringsFile(RelicStrings.class, path + "relics.json");
    }

    @Override
    public void receiveEditCards() {
        CardRegistry.registerCards();
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new ChevalGrandHat(), ChevalGrandEnums.CARD_COLOR);
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new ChevalGrand(), IMAGES + "character/select-icon.png",
                IMAGES + "character/select-background.jpg", ChevalGrandEnums.PLAYER_CLASS);
    }

    @Override
    public void receivePostInitialize() {
        LOGGER.info("ChevalGrandSlay initialized");
    }
}
