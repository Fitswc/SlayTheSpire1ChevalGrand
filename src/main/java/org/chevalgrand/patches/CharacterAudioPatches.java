package org.chevalgrand.patches;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.MonsterGroup;
import com.megacrit.cardcrawl.screens.DeathScreen;
import com.megacrit.cardcrawl.screens.charSelect.CharacterSelectScreen;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import javassist.CtBehavior;
import org.chevalgrand.characters.ChevalGrandAudio;
import org.chevalgrand.characters.ChevalGrandEnums;

public final class CharacterAudioPatches {
    private CharacterAudioPatches() {}

    @SpirePatch(clz = CharacterSelectScreen.class, method = "deselectOtherOptions")
    public static class Select {
        public static void Prefix() {
            ChevalGrandAudio.stopIntroduction();
        }
    }

    @SpirePatch(clz = CharacterSelectScreen.class, method = "updateButtons")
    public static class Embark {
        @SpireInsertPatch(locator = Locator.class)
        public static void Insert() {
            ChevalGrandAudio.embark();
        }

        public static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior behavior) throws Exception {
                return LineFinder.findInOrder(behavior, new Matcher.MethodCallMatcher(MainMenuScreen.class, "fadeOutMusic"));
            }
        }
    }

    @SpirePatch(clz = DeathScreen.class, method = SpirePatch.CONSTRUCTOR, paramtypez = {MonsterGroup.class})
    public static class Death {
        public static void Postfix() {
            if (AbstractDungeon.player != null && AbstractDungeon.player.chosenClass == ChevalGrandEnums.PLAYER_CLASS) {
                ChevalGrandAudio.death();
            }
        }
    }
}
