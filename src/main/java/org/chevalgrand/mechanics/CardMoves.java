package org.chevalgrand.mechanics;

import basemod.BaseMod;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.GameActionManager;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.utility.NewQueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;
import org.chevalgrand.cards.ChaseTheWindDiscardPile;
import org.chevalgrand.cards.ChaseTheWindDrawPile;
import org.chevalgrand.cards.ChaseTheWindRestPile;
import org.chevalgrand.cards.RaceCard;

public final class CardMoves {
    private static final Map<RaceCard, Replay> REPLAYS = new IdentityHashMap<>();
    private static RaceCard attacking;

    private CardMoves() {}

    public static AbstractGameAction run(Runnable operation) {
        return new AbstractGameAction() {
            @Override
            public void update() {
                operation.run();
                isDone = true;
            }
        };
    }

    public static CardSelection hand(AbstractCard source, Predicate<AbstractCard> condition, Consumer<AbstractCard> effect) {
        return new CardSelection(() -> options(AbstractDungeon.player.hand, c -> c != source && condition.test(c)),
                effect, 1, false);
    }

    public static CardSelection rest(int count, boolean optional, Predicate<RaceCard> filter, Consumer<RaceCard> effect) {
        return new CardSelection(() -> {
            CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            for (RaceCard card : Stamina.restingCards()) {
                if (filter.test(card)) choices.addToBottom(card);
            }
            return choices;
        }, c -> effect.accept((RaceCard)c), count, optional);
    }

    private static CardGroup options(CardGroup group, Predicate<AbstractCard> filter) {
        CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
        for (AbstractCard card : group.group) {
            if (filter.test(card)) choices.addToBottom(card);
        }
        return choices;
    }

    public static void discard(AbstractCard card) {
        AbstractDungeon.player.hand.moveToDiscardPile(card);
        card.triggerOnManualDiscard();
        GameActionManager.incrementDiscard(false);
    }

    public static CardSelection fetch(int count) {
        return new CardSelection(() -> {
            AbstractPlayer p = AbstractDungeon.player;
            CardGroup choices = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            int slots = BaseMod.MAX_HAND_SIZE - p.hand.size();
            if (slots <= 0) return choices;
            choices.group.addAll(p.drawPile.group);
            choices.group.addAll(p.discardPile.group);
            return choices;
        }, c -> {
            AbstractPlayer p = AbstractDungeon.player;
            if (p.hand.size() >= BaseMod.MAX_HAND_SIZE) return;
            p.drawPile.removeCard(c);
            p.discardPile.removeCard(c);
            p.hand.addToTop(c);
            if (c.cost >= 0) c.setCostForTurn(c.costForTurn - 1);
            p.hand.applyPowers();
        }, Math.max(0, Math.min(count, BaseMod.MAX_HAND_SIZE - AbstractDungeon.player.hand.size() + 1)), false);
    }

    public static CardSelection choosePile(int count) {
        Map<AbstractCard, Runnable> choices = new IdentityHashMap<>();
        return new CardSelection(() -> {
            AbstractPlayer p = AbstractDungeon.player;
            CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
            if (!p.drawPile.isEmpty()) {
                pileChoice(group, choices, new ChaseTheWindDrawPile(),
                        () -> AbstractDungeon.actionManager.addToTop(new DrawCardAction(Math.min(count, p.drawPile.size()))));
            }
            if (!p.discardPile.isEmpty()) {
                pileChoice(group, choices, new ChaseTheWindDiscardPile(), () -> drawDiscard(p, count));
            }
            if (!Stamina.restingCards().isEmpty()) {
                pileChoice(group, choices, new ChaseTheWindRestPile(), () -> drawRest(p, count));
            }
            return group;
        }, c -> choices.get(c).run(), 1, false);
    }

    private static void pileChoice(CardGroup group, Map<AbstractCard, Runnable> choices, AbstractCard option, Runnable effect) {
        group.addToBottom(option);
        choices.put(option, effect);
    }

    private static void drawDiscard(AbstractPlayer player, int count) {
        if (player.hasPower("No Draw")) return;
        while (count-- > 0 && !player.discardPile.isEmpty() && player.hand.size() < BaseMod.MAX_HAND_SIZE) {
            int index = AbstractDungeon.cardRandomRng.random(player.discardPile.size() - 1);
            drawn(player, player.discardPile.group.get(index));
        }
    }

    private static void drawRest(AbstractPlayer player, int count) {
        if (player.hasPower("No Draw")) return;
        while (count-- > 0 && !Stamina.restingCards().isEmpty() && player.hand.size() < BaseMod.MAX_HAND_SIZE) {
            RaceCard card = Stamina.restingCards().get(AbstractDungeon.cardRandomRng.random(Stamina.restingCards().size() - 1));
            Stamina.leaveRest(card);
            drawn(player, card);
        }
    }

    private static void drawn(AbstractPlayer player, AbstractCard card) {
        player.discardPile.removeCard(card);
        player.hand.addToTop(card);
        card.triggerWhenDrawn();
        new ArrayList<>(player.powers).forEach(power -> power.onCardDraw(card));
        player.hand.applyPowers();
    }

    public static AbstractGameAction cleanse(RaceCard card, int stacks) {
        return run(() -> {
            AbstractPlayer p = AbstractDungeon.player;
            int removed = 0;
            int remaining = stacks;
            for (AbstractPower power : new ArrayList<>(p.powers)) {
                if (remaining <= 0) break;
                if (power.type != AbstractPower.PowerType.DEBUFF || power.amount <= 0) continue;
                int take = Math.min(remaining, power.amount);
                remaining -= take;
                removed += take;
                if (take == power.amount) {
                    power.onRemove();
                    p.powers.remove(power);
                } else {
                    power.reducePower(take);
                    power.updateDescription();
                }
            }
            if (removed > 0) {
                int base = card.baseDamage;
                card.baseDamage = 10 * removed;
                card.calculateCardDamage(null);
                card.baseDamage = base;
                AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(p, card.multiDamage,
                        DamageInfo.DamageType.NORMAL, RaceCard.SLASH));
            }
        });
    }

    public static void play(RaceCard card, int times, AbstractMonster target, boolean refill, boolean extraHit) {
        if (times <= 0) return;
        REPLAYS.put(card, new Replay(times, target, refill, extraHit));
        AbstractDungeon.actionManager.addToTop(queue(card, target));
    }

    private static NewQueueCardAction queue(RaceCard card, AbstractMonster target) {
        AbstractPlayer player = AbstractDungeon.player;
        player.discardPile.removeCard(card);
        player.exhaustPile.removeCard(card);
        Stamina.leaveRest(card);
        // The native queue takes cards in hand or in limbo and drives normal use-card triggers.
        if (!player.hand.contains(card) && !player.limbo.contains(card)) player.limbo.addToBottom(card);
        card.freeToPlayOnce = true;
        if (target == null || target.isDeadOrEscaped()) return new NewQueueCardAction(card, true, false, true);
        return new NewQueueCardAction(card, target, false, true);
    }

    private static boolean takeExtraHit(RaceCard card) {
        Replay replay = REPLAYS.get(card);
        if (replay == null || !replay.extraHit) return false;
        replay.extraHit = false;
        return true;
    }

    public static void clear() {
        attacking = null;
        REPLAYS.clear();
    }

    public static void beginAttack(RaceCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) attacking = card;
    }

    public static void finishAttack(UseCardAction action, AbstractCard card) {
        if (action.isDone && attacking != null && attacking == card) {
            attacking.clearAttackBonus();
            attacking = null;
        }
    }

    public static void extraHit(DamageAction action, DamageInfo info) {
        if (action.isDone && attacking != null && info.owner == AbstractDungeon.player
                && info.type == DamageInfo.DamageType.NORMAL && takeExtraHit(attacking)) {
            AbstractDungeon.actionManager.addToTop(new DamageAction(action.target, info, action.attackEffect));
        }
    }

    public static void extraGroupHit(DamageAllEnemiesAction action) {
        if (action.isDone && attacking != null && action.source == AbstractDungeon.player
                && action.damageType == DamageInfo.DamageType.NORMAL && takeExtraHit(attacking)) {
            AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(action.source,
                    action.damage.clone(), action.damageType, action.attackEffect));
        }
    }

    public static void afterUse(UseCardAction __instance, AbstractCard ___targetCard) {
        if (!__instance.isDone || !(___targetCard instanceof RaceCard)) return;
        RaceCard card = (RaceCard) ___targetCard;
        Replay replay = REPLAYS.get(card);
        if (replay == null) return;
        if (replay.refill) {
            card.setRemaining(card.maxStamina());
            Stamina.leaveRest(card);
            AbstractPlayer p = AbstractDungeon.player;
            p.hand.removeCard(card);
            p.exhaustPile.removeCard(card);
            p.limbo.removeCard(card);
            if (!p.discardPile.contains(card)) p.discardPile.addToTop(card);
        }
        if (--replay.left > 0 && !AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
            AbstractDungeon.actionManager.addToTop(queue(card, replay.target));
        } else {
            REPLAYS.remove(card);
        }
    }

    private static final class Replay {
        int left;
        final AbstractMonster target;
        final boolean refill;
        boolean extraHit;

        Replay(int left, AbstractMonster target, boolean refill, boolean extraHit) {
            this.left = left;
            this.target = target;
            this.refill = refill;
            this.extraHit = extraHit;
        }
    }
}
