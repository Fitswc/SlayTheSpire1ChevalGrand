package org.chevalgrand;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.chevalgrand.cards.CardRegistry;
import org.chevalgrand.characters.ChevalGrand;
import org.chevalgrand.characters.ChevalGrandEnums;
import org.chevalgrand.keywords.RaceKeywords;
import org.chevalgrand.potions.RacePotions;
import org.chevalgrand.powers.PowerHooks;
import org.chevalgrand.relics.ChevalGrandHat;
import org.chevalgrand.relics.ChevalGrandHatPlus;
import org.chevalgrand.rewards.StaminaReward;

@SpireInitializer
public final class ChevalGrandSlayMod implements EditCardsSubscriber, EditCharactersSubscriber,
        EditRelicsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber, PostBattleSubscriber,
        PostInitializeSubscriber {
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
        BaseMod.loadCustomStringsFile(PowerStrings.class, path + "powers.json");
        BaseMod.loadCustomStringsFile(PotionStrings.class, path + "potions.json");
        BaseMod.loadCustomStringsFile(UIStrings.class, path + "ui.json");
    }

    @Override
    public void receiveEditKeywords() {
        RaceKeywords.register();
    }

    @Override
    public void receiveEditCards() {
        CardRegistry.registerCards();
    }

    @Override
    public void receiveEditRelics() {
        BaseMod.addRelicToCustomPool(new ChevalGrandHat(), ChevalGrandEnums.CARD_COLOR);
        BaseMod.addRelicToCustomPool(new ChevalGrandHatPlus(), ChevalGrandEnums.CARD_COLOR);
    }

    @Override
    public void receiveEditCharacters() {
        BaseMod.addCharacter(new ChevalGrand(), IMAGES + "characters/select-icon.png",
                IMAGES + "characters/select-background.jpg", ChevalGrandEnums.PLAYER_CLASS);
    }

    @Override
    public void receivePostInitialize() {
        RacePotions.register();
        StaminaReward.register();
        LOGGER.info("ChevalGrandSlay initialized");
    }

    @Override
    public void receivePostBattle(AbstractRoom room) {
        StaminaReward.offer(room);
        PowerHooks.resetCombat();
    }
}
