package org.chevalgrand.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.ui.panels.TopPanel;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import org.chevalgrand.characters.ChevalGrand;

@SpirePatch(clz = TopPanel.class, method = "setPlayerName")
public final class ChevalTopPanelPatch {
    @SpireInstrumentPatch
    public static ExprEditor useCharacterName() {
        return new ExprEditor() {
            @Override
            public void edit(FieldAccess field) throws CannotCompileException {
                if (!field.isReader()) return;
                if (field.getClassName().equals(CardCrawlGame.class.getName()) && field.getFieldName().equals("playerName")) {
                    field.replace("{ $_ = org.chevalgrand.patches.ChevalTopPanelPatch.displayName($proceed()); }");
                } else if (field.getClassName().equals(AbstractPlayer.class.getName()) && field.getFieldName().equals("title")) {
                    field.replace("{ $_ = org.chevalgrand.patches.ChevalTopPanelPatch.displayTitle($proceed()); }");
                }
            }
        };
    }

    public static String displayName(String name) {
        AbstractPlayer player = AbstractDungeon.player;
        return player instanceof ChevalGrand ? player.getLocalizedCharacterName() : name;
    }

    public static String displayTitle(String title) {
        return AbstractDungeon.player instanceof ChevalGrand ? "" : title;
    }
}
