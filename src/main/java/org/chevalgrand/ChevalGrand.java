package org.chevalgrand;

import basemod.animations.AbstractAnimation;
import basemod.abstracts.CustomPlayer;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import org.chevalgrand.cards.ALittleCourage;
import org.chevalgrand.cards.Defend;
import org.chevalgrand.cards.Strike;

import java.util.ArrayList;
import java.util.Collections;

import static org.chevalgrand.ChevalGrandEnums.CARD_COLOR;
import static org.chevalgrand.ChevalGrandEnums.ENERGY;
import static org.chevalgrand.ChevalGrandEnums.PLAYER_CLASS;
import static org.chevalgrand.ChevalGrandEnums.THEME;

public final class ChevalGrand extends CustomPlayer {
    private static final String ID = ChevalGrandSlayMod.id("ChevalGrand");
    private static final String ART = ChevalGrandSlayMod.IMAGES + "character/";
    private static final int STARTING_HP = 77;
    private static final int STARTING_GOLD = 99;
    private static final int ENERGY_PER_TURN = 3;
    private static final String[] ORB_LAYERS = {
            ENERGY + "layer-1.png", ENERGY + "layer-2.png", ENERGY + "layer-3.png",
            ENERGY + "layer-4.png", ENERGY + "layer-5.png", ENERGY + "orb-base.png",
            ENERGY + "layer-1-off.png", ENERGY + "layer-2-off.png", ENERGY + "layer-3-off.png",
            ENERGY + "layer-4-off.png", ENERGY + "layer-5-off.png"
    };
    private static final float[] ORB_SPEEDS = {0f, 12f, -22f, 8f, 0f};

    ChevalGrand() {
        super(strings().NAMES[0], PLAYER_CLASS, ORB_LAYERS, ENERGY + "orb-vfx.png", ORB_SPEEDS,
                new AbstractAnimation() {
                    @Override
                    public Type type() {
                        return Type.NONE;
                    }
                });
        initializeClass(ART + "combat.png", ART + "campfire.png", ART + "campfire.png", ART + "fallen.png",
                getLoadout(), 0f, 0f, 220f, 295f, new EnergyManager(ENERGY_PER_TURN));
    }

    private static CharacterStrings strings() {
        return CardCrawlGame.languagePack.getCharacterString(ID);
    }

    @Override
    public CharSelectInfo getLoadout() {
        return new CharSelectInfo(getLocalizedCharacterName(), strings().TEXT[0], STARTING_HP,
                STARTING_HP, 0, STARTING_GOLD, 5, this, getStartingRelics(), getStartingDeck(), false);
    }

    @Override
    public ArrayList<String> getStartingDeck() {
        ArrayList<String> deck = new ArrayList<>(10);
        deck.addAll(Collections.nCopies(5, Strike.ID));
        deck.addAll(Collections.nCopies(4, Defend.ID));
        deck.add(ALittleCourage.ID);
        return deck;
    }

    @Override
    public ArrayList<String> getStartingRelics() {
        UnlockTracker.markRelicAsSeen(ChevalGrandHat.ID);
        return new ArrayList<>(Collections.singletonList(ChevalGrandHat.ID));
    }

    @Override
    public ArrayList<AbstractCard> getCardPool(ArrayList<AbstractCard> pool) {
        super.getCardPool(pool);
        pool.removeIf(card -> card.color == CARD_COLOR && card.rarity == AbstractCard.CardRarity.SPECIAL);
        return pool;
    }

    @Override
    public AbstractCard getStartCardForEvent() {
        return new ALittleCourage();
    }

    @Override
    public AbstractCard.CardColor getCardColor() {
        return CARD_COLOR;
    }

    @Override
    public String getLocalizedCharacterName() {
        return strings().NAMES[0];
    }

    @Override
    public String getTitle(PlayerClass playerClass) {
        return getLocalizedCharacterName();
    }

    @Override
    public CharacterStrings getCharacterString() {
        return strings();
    }

    @Override
    public String getPortraitImageName() {
        return ART + "select-background.jpg";
    }

    @Override
    public Color getCardRenderColor() {
        return THEME.cpy();
    }

    @Override
    public Color getCardTrailColor() {
        return THEME.cpy();
    }

    @Override
    public Color getSlashAttackColor() {
        return THEME.cpy();
    }

    @Override
    public BitmapFont getEnergyNumFont() {
        return FontHelper.energyNumFontBlue;
    }

    @Override
    public int getAscensionMaxHPLoss() {
        return 4;
    }

    @Override
    public String getSpireHeartText() {
        return strings().TEXT[1];
    }

    @Override
    public AttackEffect[] getSpireHeartSlashEffect() {
        return new AttackEffect[] {AttackEffect.BLUNT_LIGHT, AttackEffect.BLUNT_HEAVY,
                AttackEffect.SLASH_DIAGONAL, AttackEffect.SLASH_HEAVY};
    }

    @Override
    public String getVampireText() {
        return strings().TEXT[2];
    }

    @Override
    public void doCharSelectScreenSelectEffect() {
        CardCrawlGame.sound.play(getCustomModeCharacterButtonSoundKey());
    }

    @Override
    public String getCustomModeCharacterButtonSoundKey() {
        return "ATTACK_FAST";
    }

    @Override
    public ChevalGrand newInstance() {
        return new ChevalGrand();
    }
}
