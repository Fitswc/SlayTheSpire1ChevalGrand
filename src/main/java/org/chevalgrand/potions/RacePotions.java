package org.chevalgrand.potions;

import basemod.BaseMod;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPotion;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndAddToHandEffect;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.cards.RaceCard;
import org.chevalgrand.characters.ChevalGrandEnums;
import org.chevalgrand.keywords.RaceKeywords;
import org.chevalgrand.mechanics.Stamina;
import org.chevalgrand.powers.EndlessStaminaPower;
import org.chevalgrand.relics.ChevalGrandHatPlus;

public final class RacePotions {
    private static final Map<String, Texture> ART = new HashMap<>();

    private RacePotions() {}

    public static void register() {
        BaseMod.addPotion(EndlessStamina.class, Color.CLEAR, null, null, EndlessStamina.ID, ChevalGrandEnums.PLAYER_CLASS);
        BaseMod.addPotion(Potential.class, Color.CLEAR, null, null, Potential.ID, ChevalGrandEnums.PLAYER_CLASS);
    }

    private abstract static class RacePotion extends CustomPotion {
        RacePotion(String id, PotionRarity rarity, String art) {
            super(strings(id).NAME, id, rarity, PotionSize.BOTTLE, PotionColor.NONE);
            ReflectionHacks.setPrivate(this, AbstractPotion.class, "containerImg", image(art));
            ReflectionHacks.setPrivate(this, AbstractPotion.class, "outlineImg", image(art + "-outline"));
            ReflectionHacks.setPrivate(this, AbstractPotion.class, "liquidColor", Color.CLEAR.cpy());
        }

        private static Texture image(String art) {
            return ART.computeIfAbsent(art, key -> ImageMaster.loadImage(ChevalGrandSlayMod.IMAGES + "potions/" + key + ".png"));
        }

        private static PotionStrings strings(String id) {
            return CardCrawlGame.languagePack.getPotionString(id);
        }

        @Override
        public final void initializeData() {
            potency = getPotency();
            description = strings(ID).DESCRIPTIONS[0];
            tips.clear();
            tips.add(new PowerTip(name, description));
            String[] stamina = RaceKeywords.text(RaceKeywords.STAMINA);
            tips.add(new PowerTip(stamina[0], stamina[1]));
        }

        @Override
        public final boolean canUse() {
            return super.canUse() && AbstractDungeon.getCurrRoom().phase == AbstractRoom.RoomPhase.COMBAT;
        }

        @Override
        public final int getPotency(int ascensionLevel) {
            return 1;
        }
    }

    public static final class EndlessStamina extends RacePotion {
        public static final String ID = ChevalGrandSlayMod.id("EndlessStaminaPotion");

        public EndlessStamina() {
            super(ID, PotionRarity.UNCOMMON, "endless-stamina");
        }

        @Override
        public void use(AbstractCreature target) {
            AbstractPlayer player = AbstractDungeon.player;
            addToBot(new ApplyPowerAction(player, player, new EndlessStaminaPower(player, 1)));
        }

        @Override
        public EndlessStamina makeCopy() {
            return new EndlessStamina();
        }
    }

    public static final class Potential extends RacePotion {
        public static final String ID = ChevalGrandSlayMod.id("PotentialPotion");

        public Potential() {
            super(ID, PotionRarity.RARE, "potential");
        }

        @Override
        public void use(AbstractCreature target) {
            addToBot(new ChoosePotentialCard());
        }

        @Override
        public Potential makeCopy() {
            return new Potential();
        }
    }

    private static final class ChoosePotentialCard extends AbstractGameAction {
        private boolean opened;

        @Override
        public void update() {
            if (!opened) {
                List<AbstractCard> pool = CardLibrary.getAllCards().stream()
                        .filter(card -> card.color == ChevalGrandEnums.CARD_COLOR && Stamina.has(card, 0))
                        .filter(card -> card.rarity != AbstractCard.CardRarity.SPECIAL
                                && !UnlockTracker.isCardLocked(card.cardID))
                        .sorted(Comparator.comparing(card -> card.cardID))
                        .collect(Collectors.toList());
                ArrayList<AbstractCard> options = new ArrayList<>(3);
                while (!pool.isEmpty() && options.size() < 3) {
                    int index = AbstractDungeon.cardRandomRng.random(pool.size() - 1);
                    RaceCard card = (RaceCard) pool.remove(index).makeCopy();
                    card.updateCost(-card.cost);
                    card.grantCombatRetain();
                    card.fillCombatStamina(999);
                    if (AbstractDungeon.player.hasRelic(ChevalGrandHatPlus.ID)) card.applyHatBonus();
                    options.add(card);
                }
                if (options.isEmpty()) {
                    isDone = true;
                    return;
                }
                AbstractDungeon.cardRewardScreen.customCombatOpen(options, RaceKeywords.text("Potential")[0], false);
                opened = true;
                return;
            }
            AbstractCard card = AbstractDungeon.cardRewardScreen.discoveryCard;
            if (card != null) {
                AbstractDungeon.cardRewardScreen.discoveryCard = null;
                AbstractDungeon.effectList.add(new ShowCardAndAddToHandEffect(card));
            }
            isDone = true;
        }
    }
}
