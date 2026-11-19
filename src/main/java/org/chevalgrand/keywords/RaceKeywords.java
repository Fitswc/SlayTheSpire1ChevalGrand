package org.chevalgrand.keywords;

import basemod.BaseMod;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import org.chevalgrand.ChevalGrandSlayMod;

public final class RaceKeywords {
    public static final String STAMINA = "Stamina";
    public static final String REST_PILE = "RestPile";

    private RaceKeywords() {}

    public static String[] text(String key) {
        return CardCrawlGame.languagePack.getUIString(ChevalGrandSlayMod.id(key)).TEXT;
    }

    public static void register() {
        for (String key : new String[] {STAMINA, REST_PILE}) {
            String[] entry = text(key);
            BaseMod.addKeyword("chevalgrandslay", entry[0], new String[] {entry[0].toLowerCase(Locale.ROOT)}, entry[1]);
        }
    }

    public static List<TooltipInfo> cardTips(boolean hasStamina, String description) {
        List<TooltipInfo> tips = new ArrayList<>();
        String[] stamina = text(STAMINA);
        String[] restPile = text(REST_PILE);
        String normalized = description.toLowerCase(Locale.ROOT);
        if (hasStamina || normalized.contains(stamina[0].toLowerCase(Locale.ROOT))) {
            tips.add(new TooltipInfo(stamina[0], stamina[1]));
        }
        if (hasStamina || normalized.contains(restPile[0].toLowerCase(Locale.ROOT))) {
            tips.add(new TooltipInfo(restPile[0], restPile[1]));
        }
        return tips;
    }
}
