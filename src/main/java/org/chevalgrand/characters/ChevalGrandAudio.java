package org.chevalgrand.characters;

import basemod.BaseMod;
import basemod.interfaces.PostUpdateSubscriber;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.screens.mainMenu.MainMenuScreen;
import java.util.concurrent.ThreadLocalRandom;
import org.chevalgrand.ChevalGrandSlayMod;

public final class ChevalGrandAudio implements PostUpdateSubscriber {
    private static final String[] INTRODUCTIONS = {
            ChevalGrandSlayMod.id("Introduce1"), ChevalGrandSlayMod.id("Introduce2"), ChevalGrandSlayMod.id("Introduce3")
    };
    private static final String TRANSITION = ChevalGrandSlayMod.id("Transition");
    private static final String DEATH = ChevalGrandSlayMod.id("Death");
    private static String activeIntro;
    private static long introHandle;

    public static void registerAudio() {
        for (int i = 0; i < INTRODUCTIONS.length; i++) {
            BaseMod.addAudio(INTRODUCTIONS[i], "ChevalGrandSlay/audio/introduce-" + (i + 1) + ".wav");
        }
        BaseMod.addAudio(TRANSITION, "ChevalGrandSlay/audio/transition.wav");
        BaseMod.addAudio(DEATH, "ChevalGrandSlay/audio/death.wav");
    }

    public static void introduce() {
        stopIntroduction();
        activeIntro = INTRODUCTIONS[ThreadLocalRandom.current().nextInt(INTRODUCTIONS.length)];
        introHandle = CardCrawlGame.sound.play(activeIntro);
    }

    public static void stopIntroduction() {
        if (activeIntro == null) return;
        if (introHandle != -1L) CardCrawlGame.sound.stop(activeIntro, introHandle);
        activeIntro = null;
    }

    public static void embark() {
        stopIntroduction();
        if (CardCrawlGame.chosenCharacter == ChevalGrandEnums.PLAYER_CLASS) CardCrawlGame.sound.play(TRANSITION);
    }

    public static void death() {
        CardCrawlGame.sound.play(DEATH);
    }

    @Override
    public void receivePostUpdate() {
        if (activeIntro == null) return;
        if (CardCrawlGame.mode != CardCrawlGame.GameMode.CHAR_SELECT || CardCrawlGame.mainMenuScreen == null
                || CardCrawlGame.mainMenuScreen.screen != MainMenuScreen.CurScreen.CHAR_SELECT) stopIntroduction();
    }
}
