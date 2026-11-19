package org.chevalgrand.rewards;

import basemod.BaseMod;
import basemod.abstracts.CustomReward;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireEnum;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.random.Random;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.rewards.RewardSave;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;
import com.megacrit.cardcrawl.unlock.UnlockTracker;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardBrieflyEffect;
import com.megacrit.cardcrawl.vfx.cardManip.ShowCardAndObtainEffect;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.cards.RaceCard;
import org.chevalgrand.characters.ChevalGrandEnums;
import org.chevalgrand.keywords.RaceKeywords;
import org.chevalgrand.mechanics.DeckSelection;
import org.chevalgrand.mechanics.Stamina;

public final class StaminaReward extends CustomReward {
    public static final class Types {
        @SpireEnum public static RewardType CGS_STAMINA_CARD;
        @SpireEnum public static RewardType CGS_RESTORE_STAMINA;
    }

    private static final AbstractCard.CardRarity[] RARITIES = {
            AbstractCard.CardRarity.COMMON, AbstractCard.CardRarity.UNCOMMON, AbstractCard.CardRarity.RARE
    };
    private static final int[] WEIGHTS = {50, 40, 10};
    private static Texture addIcon;
    private static Texture restoreIcon;

    private final boolean restore;
    private final List<String> cardIds;

    private StaminaReward(boolean restore, List<String> cardIds) {
        super(restore ? restoreIcon : addIcon, label(restore), restore ? Types.CGS_RESTORE_STAMINA : Types.CGS_STAMINA_CARD);
        this.restore = restore;
        this.cardIds = cardIds;
    }

    public static void register() {
        addIcon = ImageMaster.loadImage(ChevalGrandSlayMod.IMAGES + "rewards/stamina-card.png");
        restoreIcon = ImageMaster.loadImage(ChevalGrandSlayMod.IMAGES + "rewards/restore-stamina.png");
        BaseMod.registerCustomReward(Types.CGS_STAMINA_CARD, save -> {
            List<String> ids = new ArrayList<>();
            if (save.id != null) {
                for (String id : save.id.split("\\|")) {
                    if (Stamina.has(CardLibrary.getCard(id), 0) && !ids.contains(id) && ids.size() < 3) ids.add(id);
                }
            }
            return new StaminaReward(false, ids);
        }, reward -> new RewardSave(Types.CGS_STAMINA_CARD.name(), String.join("|", ((StaminaReward) reward).cardIds)));
        BaseMod.registerCustomReward(Types.CGS_RESTORE_STAMINA, save -> new StaminaReward(true, new ArrayList<>()),
                reward -> new RewardSave(Types.CGS_RESTORE_STAMINA.name(), ""));
    }

    public static void offer(AbstractRoom room) {
        if (CardCrawlGame.loadingSave || AbstractDungeon.loading_post_combat || !room.rewardAllowed
                || room.smoked || room.mugged || AbstractDungeon.player.chosenClass != ChevalGrandEnums.PLAYER_CLASS
                || AbstractDungeon.player.isDead || room.rewards.stream().anyMatch(StaminaReward.class::isInstance)) return;

        List<String> ids = rollCardIds();
        if (!ids.isEmpty()) room.rewards.add(new StaminaReward(false, ids));
        if (AbstractDungeon.player.masterDeck.group.stream().anyMatch(Stamina::canRestoreDeckCard)) {
            room.rewards.add(new StaminaReward(true, new ArrayList<>()));
        }
    }

    static List<String> rollCardIds() {
        List<AbstractCard> pool = CardLibrary.getAllCards().stream()
                .filter(card -> card.color == ChevalGrandEnums.CARD_COLOR && Stamina.has(card, 0))
                .filter(card -> !UnlockTracker.isCardLocked(card.cardID))
                .sorted(Comparator.comparing(card -> card.cardID))
                .collect(Collectors.toList());
        List<List<AbstractCard>> buckets = new ArrayList<>();
        for (AbstractCard.CardRarity rarity : RARITIES) {
            buckets.add(pool.stream().filter(card -> card.rarity == rarity).collect(Collectors.toList()));
        }
        Random rng = new Random(Settings.seed + 0x434753L + AbstractDungeon.floorNum);
        List<String> result = new ArrayList<>(3);
        while (result.size() < 3) {
            int weight = 0;
            for (int i = 0; i < WEIGHTS.length; i++) {
                if (!buckets.get(i).isEmpty()) weight += WEIGHTS[i];
            }
            if (weight == 0) break;
            int roll = rng.random(weight - 1);
            for (int i = 0; i < WEIGHTS.length; i++) {
                List<AbstractCard> bucket = buckets.get(i);
                if (bucket.isEmpty()) continue;
                roll -= WEIGHTS[i];
                if (roll < 0) {
                    result.add(bucket.remove(rng.random(bucket.size() - 1)).cardID);
                    break;
                }
            }
        }
        return result;
    }

    private static String label(boolean restore) {
        return RaceKeywords.text("StaminaReward")[restore ? 1 : 0];
    }

    private CardGroup choices() {
        CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        if (restore) {
            AbstractDungeon.player.masterDeck.group.stream().filter(Stamina::canRestoreDeckCard)
                    .forEach(choices::addToBottom);
        } else {
            for (String id : cardIds) {
                UnlockTracker.markCardAsSeen(id);
                choices.addToBottom(CardLibrary.getCard(id).makeCopy());
            }
        }
        return choices;
    }

    @Override
    public boolean claimReward() {
        CardGroup choices = choices();
        if (choices.isEmpty()) return true;
        AbstractDungeon.overlayMenu.proceedButton.hide();
        DeckSelection.open(choices, 1, RaceKeywords.text("StaminaReward")[restore ? 3 : 2], selected -> {
            if (selected.isEmpty()) return;
            AbstractCard card = selected.get(0);
            if (restore) {
                if (!AbstractDungeon.player.masterDeck.contains(card) || !Stamina.canRestoreDeckCard(card)) return;
                RaceCard race = (RaceCard) card;
                race.setRemaining(race.maxStamina());
                race.clearRestOrder();
                AbstractDungeon.topLevelEffects.add(new ShowCardBrieflyEffect(card.makeStatEquivalentCopy()));
            } else {
                AbstractDungeon.topLevelEffects.add(new ShowCardAndObtainEffect(card,
                        Settings.WIDTH / 2f, Settings.HEIGHT / 2f));
            }
            AbstractDungeon.getCurrRoom().rewards.removeIf(StaminaReward.class::isInstance);
            CombatRewardScreen screen = AbstractDungeon.combatRewardScreen;
            screen.rewards.removeIf(StaminaReward.class::isInstance);
            screen.positionRewards();
            screen.reopen();
        });
        return false;
    }

    @SpirePatch(clz = CombatRewardScreen.class, method = "setupItemReward")
    public static class RewardLabels {
        public static void Postfix(CombatRewardScreen __instance) {
            if (__instance.rewards.stream().noneMatch(StaminaReward.class::isInstance)) return;
            __instance.rewards.removeIf(item -> item instanceof StaminaReward && !((StaminaReward) item).canClaim());
            long count = __instance.rewards.stream().filter(StaminaReward.class::isInstance).count();
            for (RewardItem item : __instance.rewards) {
                if (item instanceof StaminaReward) {
                    item.text = label(((StaminaReward) item).restore) + (count > 1 ? RaceKeywords.text("StaminaReward")[4] : "");
                }
            }
            __instance.positionRewards();
        }
    }

    private boolean canClaim() {
        return restore ? AbstractDungeon.player.masterDeck.group.stream().anyMatch(Stamina::canRestoreDeckCard)
                : !cardIds.isEmpty();
    }
}
