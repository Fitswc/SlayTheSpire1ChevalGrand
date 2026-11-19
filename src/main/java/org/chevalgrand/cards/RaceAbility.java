package org.chevalgrand.cards;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.actions.watcher.SkipEnemiesTurnAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import org.chevalgrand.ChevalGrandSlayMod;

import java.util.ArrayDeque;
import java.util.ArrayList;

final class RaceAbility extends AbstractPower {
    enum Effect {
        BORROW("BorrowFromThePast"), RESIST("BorrowForceEscape"), VIGOR("ClamToFight"),
        COUNTER("CounterAttack"), DOMAIN("Domain"), OVERFLOW("EchoOfTheFinish"),
        FILL("FillTheGap"), FINISH("FinishLineEcho"), FORGE("ForgeAhead"),
        ENERGY("KeepSomeInReserve"), CAP("MeasuredAdvanceAndRetreat"), FULL_COUNTER("NoConcession"),
        NOREGRETS("NoRegretsEnding"), EXTRA_TURN("OneBeatAhead"), PERFECT("PrefectDefend"),
        PACT("RenewTheOldPact"), NO_POSITIVE("SuppressTheirSpirit"),
        TOGETHER("TurnTogether"), WIDE("WideView"), ENDLESS("TurnTogether");

        final String card;
        final String id;
        Effect(String card) {
            this.card = card;
            id = ChevalGrandSlayMod.id("Power:" + name());
        }
    }

    final Effect effect;
    int recovery = 1;
    private int pending;
    private int loss;
    private int usedTurn = -1;
    private int carry;
    private static int handLimitBonus;
    private static RaceCard attacking;
    private static final ArrayDeque<Integer> BEFORE_BLOCK = new ArrayDeque<>();
    private static final ArrayDeque<Integer> BEFORE_MONSTER_HP = new ArrayDeque<>();

    RaceAbility(Effect effect, AbstractCreature owner, int amount) {
        this.effect = effect;
        this.owner = owner;
        this.amount = amount;
        ID = effect.id;
        name = CardCrawlGame.languagePack.getCardStrings(ChevalGrandSlayMod.id(effect.card)).NAME;
        type = effect == Effect.NO_POSITIVE ? PowerType.DEBUFF : PowerType.BUFF;
        String art = ChevalGrandSlayMod.IMAGES + "powers/" + effect.card + ".png";
        Texture texture = ImageMaster.loadImage(art);
        if (texture != null) region48 = region128 = new TextureAtlas.AtlasRegion(texture, 0, 0,
                texture.getWidth(), texture.getHeight());
        updateDescription();
    }

    @Override
    public void updateDescription() {
        com.megacrit.cardcrawl.localization.CardStrings strings =
                CardCrawlGame.languagePack.getCardStrings(ChevalGrandSlayMod.id(effect.card));
        String text = strings.DESCRIPTION;
        if (((effect == Effect.FILL || effect == Effect.PACT) && amount >= 2
                || effect == Effect.TOGETHER && amount > 0) && strings.UPGRADE_DESCRIPTION != null) {
            text = strings.UPGRADE_DESCRIPTION;
        }
        description = text.replace("!M!", Integer.toString(amount)).replace(" NL ", " ");
    }

    @Override
    public void onInitialApplication() {
        if (effect == Effect.WIDE) changeHandLimit(amount);
        if (effect == Effect.DOMAIN) {
            addToBot(new GainEnergyAction(4));
            addToBot(CardMoves.run(() -> {
                for (RaceCard card : new ArrayList<>(Stamina.REST)) {
                    if (!Stamina.returnToHand(card, true, false)) Stamina.moveToDraw(card, card.maxStamina);
                }
            }));
        }
    }

    @Override
    public void stackPower(int extra) {
        if (effect == Effect.BORROW || effect == Effect.EXTRA_TURN) return;
        if (effect == Effect.PACT || effect == Effect.TOGETHER) {
            amount = Math.max(amount, extra);
            updateDescription();
            return;
        }
        if (effect == Effect.WIDE) changeHandLimit(extra);
        super.stackPower(extra);
    }

    @Override
    public void onRemove() {
        if (effect == Effect.WIDE) changeHandLimit(-Math.min(amount, handLimitBonus));
    }

    private static void changeHandLimit(int delta) {
        handLimitBonus += delta;
        BaseMod.MAX_HAND_SIZE += delta;
    }

    static void resetCombat() {
        if (handLimitBonus != 0) changeHandLimit(-handLimitBonus);
        attacking = null;
        CardMoves.clear();
    }

    @Override
    public void atStartOfTurn() {
        switch (effect) {
            case BORROW:
                block(2 * Stamina.REST.size());
                break;
            case PERFECT:
                block(amount);
                break;
            case PACT:
                addToBot(new GainEnergyAction(1));
                addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 4)));
                if (amount >= 2) addToBot(new DrawCardAction(1));
                break;
            case WIDE:
                for (RaceCard card : new ArrayList<>(Stamina.REST.subList(0,
                        Math.min(recovery + 1, Stamina.REST.size())))) {
                    Stamina.moveToDraw(card, recovery);
                }
                break;
            case EXTRA_TURN:
                if (pending > 0) {
                    addToBot(new ApplyPowerAction(owner, owner, new StrengthPower(owner, 1)));
                    addToBot(new ApplyPowerAction(owner, owner, new DexterityPower(owner, 1)));
                    remove();
                }
                break;
            case CAP:
            case COUNTER:
            case FULL_COUNTER:
                remove();
                break;
            default:
                break;
        }
    }

    @Override
    public void atEndOfTurn(boolean isPlayer) {
        if (!isPlayer) return;
        if (effect == Effect.DOMAIN) {
            if (amount > 1) addToBot(new ReducePowerAction(owner, owner, ID, 1));
            else if (!AbstractDungeon.getMonsters().areMonstersBasicallyDead()) {
                addToBot(new DamageAction(owner, new DamageInfo(owner, owner.currentHealth, DamageInfo.DamageType.HP_LOSS),
                        AbstractGameAction.AttackEffect.NONE));
            }
        }
        if (effect == Effect.ENERGY) carry = EnergyPanel.totalCount;
        if (effect == Effect.EXTRA_TURN && pending == 0) {
            pending = 1;
            addToTop(new SkipEnemiesTurnAction());
        }
    }

    @Override
    public void onEnergyRecharge() {
        if (effect == Effect.DOMAIN) addToBot(new GainEnergyAction(4));
        if (effect == Effect.ENERGY && carry > 0) {
            AbstractPlayer player = (AbstractPlayer) owner;
            if (!player.hasRelic("Ice Cream") && !player.hasPower("Conserve")) addToBot(new GainEnergyAction(carry));
            carry = 0;
        }
    }

    @Override
    public int onAttackedToChangeDamage(DamageInfo info, int damageAmount) {
        if (effect != Effect.CAP || !enemyAttack(info) || !AbstractDungeon.actionManager.turnHasEnded) return damageAmount;
        int actual = Math.min(damageAmount, Math.max(0, amount - loss));
        loss += actual;
        return actual;
    }

    @Override
    public float atDamageGive(float damage, DamageInfo.DamageType type, AbstractCard card) {
        if (type != DamageInfo.DamageType.NORMAL || card == null || card.type != AbstractCard.CardType.ATTACK) return damage;
        if (effect == Effect.DOMAIN) return damage * 1.8f;
        if (effect == Effect.VIGOR) return damage + amount;
        if (effect == Effect.NOREGRETS) return damage + pending;
        return damage;
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (card.type != AbstractCard.CardType.ATTACK) return;
        if (effect == Effect.FORGE && AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .filter(c -> c.type == AbstractCard.CardType.ATTACK).count() == 3) {
            addToBot(new GainEnergyAction(1));
            all(owner, 18, false);
        }
        if (effect == Effect.VIGOR) remove();
        if (effect == Effect.NOREGRETS) pending = 0;
    }

    private void remove() {
        addToBot(new RemoveSpecificPowerAction(owner, owner, ID));
    }

    private void block(int value) {
        if (value > 0) addToBot(new GainBlockAction(owner, owner, value));
    }

    private static boolean enemyAttack(DamageInfo info) {
        return info.type == DamageInfo.DamageType.NORMAL && info.owner instanceof AbstractMonster;
    }

    static void onRestEnter() {
        AbstractPlayer p = AbstractDungeon.player;
        RaceAbility noRegrets = ability(p, Effect.NOREGRETS);
        if (noRegrets != null) noRegrets.pending += noRegrets.amount;
        RaceAbility finish = ability(p, Effect.FINISH);
        if (finish != null && !AbstractDungeon.actionManager.turnHasEnded) all(p, finish.amount, false);
    }

    static void onRestExit(RaceCard card) {
        if (AbstractDungeon.actionManager.turnHasEnded) return;
        AbstractPlayer p = AbstractDungeon.player;
        RaceAbility fill = ability(p, Effect.FILL);
        int turn = com.megacrit.cardcrawl.actions.GameActionManager.turn;
        if (fill != null && fill.usedTurn != turn) {
            fill.usedTurn = turn;
            card.remaining++;
            if (card.deckOrigin != null) card.deckOrigin.setRemaining(card.remaining);
            if (fill.amount >= 2) fill.addToBot(new GainEnergyAction(1));
        }
        RaceAbility together = ability(p, Effect.TOGETHER);
        if (together != null) {
            together.addToBot(new DrawCardAction(1));
            together.block(together.amount);
        }
    }

    private static RaceAbility ability(AbstractCreature creature, Effect effect) {
        AbstractPower p = creature.getPower(effect.id);
        return p instanceof RaceAbility ? (RaceAbility)p : null;
    }

    private static void all(AbstractCreature source, int damage, boolean first) {
        DamageAllEnemiesAction action = new DamageAllEnemiesAction(source, DamageInfo.createDamageMatrix(damage, true),
                DamageInfo.DamageType.THORNS, RaceCard.SLASH);
        if (first) AbstractDungeon.actionManager.addToTop(action);
        else AbstractDungeon.actionManager.addToBottom(action);
    }

    static void beginAttack(RaceCard card) {
        if (card.type == AbstractCard.CardType.ATTACK) attacking = card;
    }

    @SpirePatch(clz = UseCardAction.class, method = "update")
    public static class FinishAttack {
        public static void Postfix(UseCardAction __instance, AbstractCard ___targetCard) {
            if (__instance.isDone && ___targetCard == attacking && attacking != null) {
                attacking.nextAttackBonus = 0;
                attacking = null;
            }
        }
    }

    @SpirePatch(clz = DamageAction.class, method = "update")
    public static class ExtraHit {
        public static void Postfix(DamageAction __instance, DamageInfo ___info) {
            if (__instance.isDone && attacking != null && ___info.owner == AbstractDungeon.player
                    && ___info.type == DamageInfo.DamageType.NORMAL && CardMoves.takeExtraHit(attacking)) {
                AbstractDungeon.actionManager.addToTop(new DamageAction(__instance.target, ___info, __instance.attackEffect));
            }
        }
    }

    @SpirePatch(clz = DamageAllEnemiesAction.class, method = "update")
    public static class ExtraGroupHit {
        public static void Postfix(DamageAllEnemiesAction __instance) {
            if (__instance.isDone && attacking != null && __instance.source == AbstractDungeon.player
                    && __instance.damageType == DamageInfo.DamageType.NORMAL && CardMoves.takeExtraHit(attacking)) {
                AbstractDungeon.actionManager.addToTop(new DamageAllEnemiesAction(__instance.source,
                        __instance.damage.clone(), __instance.damageType, __instance.attackEffect));
            }
        }
    }

    @SpirePatch(clz = AbstractPlayer.class, method = "damage")
    public static class DetectBlocks {
        public static void Prefix(AbstractPlayer __instance, DamageInfo info) {
            BEFORE_BLOCK.push(__instance.currentBlock);
        }

        public static void Postfix(AbstractPlayer __instance, DamageInfo info) {
            int oldBlock = BEFORE_BLOCK.pop();
            if (!enemyAttack(info) || oldBlock <= __instance.currentBlock) return;
            boolean fullyBlocked = __instance.lastDamageTaken == 0;
            RaceAbility counter = ability(__instance, Effect.COUNTER);
            if (counter != null) {
                AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
                if (target != null) counter.addToBot(new DamageAction(target,
                        new DamageInfo(__instance, counter.amount, DamageInfo.DamageType.THORNS), RaceCard.SLASH));
            }
            if (!fullyBlocked) return;
            RaceAbility perfect = ability(__instance, Effect.PERFECT);
            if (perfect != null) {
                perfect.addToBot(new GainEnergyAction(1));
                perfect.addToBot(new DrawCardAction(1));
            }
            RaceAbility full = ability(__instance, Effect.FULL_COUNTER);
            if (full != null && AbstractDungeon.actionManager.turnHasEnded) all(__instance, full.amount, false);
        }
    }

    @SpirePatch(clz = AbstractMonster.class, method = "damage")
    public static class DetectOverkill {
        public static void Prefix(AbstractMonster __instance, DamageInfo info) {
            BEFORE_MONSTER_HP.push(__instance.currentHealth);
        }
        public static void Postfix(AbstractMonster __instance, DamageInfo info) {
            int oldHealth = BEFORE_MONSTER_HP.pop();
            AbstractPlayer p = AbstractDungeon.player;
            if (p == null || info.owner != p || oldHealth <= 0 || __instance.currentHealth > 0) return;
            RaceAbility overflow = ability(p, Effect.OVERFLOW);
            if (overflow == null) return;
            int excess = Math.min(overflow.amount, __instance.lastDamageTaken - oldHealth);
            if (excess > 0) all(p, excess, false);
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = "update")
    public static class ModifyIncomingPower {
        public static SpireReturn<Void> Prefix(ApplyPowerAction __instance, AbstractPower ___powerToApply) {
            AbstractCreature target = __instance.target;
            if (__instance.isDone || PowerActionState.checked.get(__instance)
                    || target == null || ___powerToApply == null || __instance.amount <= 0) {
                return SpireReturn.Continue();
            }
            PowerActionState.checked.set(__instance, true);
            if (target.hasPower(Effect.NO_POSITIVE.id) && ___powerToApply.type == PowerType.BUFF) {
                __instance.isDone = true;
                return SpireReturn.Return();
            }
            RaceAbility resist = ability(target, Effect.RESIST);
            if (resist != null && ("Weak".equals(___powerToApply.ID) || "Frail".equals(___powerToApply.ID))) {
                __instance.amount = Math.max(0, __instance.amount - resist.amount);
                ___powerToApply.amount = __instance.amount;
                if (__instance.amount == 0) {
                    __instance.isDone = true;
                    return SpireReturn.Return();
                }
            }
            RaceAbility wide = ability(target, Effect.WIDE);
            if (wide != null && ___powerToApply instanceof RaceAbility
                    && ((RaceAbility)___powerToApply).effect == Effect.WIDE) {
                wide.recovery = Math.max(wide.recovery, ((RaceAbility)___powerToApply).recovery);
            }
            return SpireReturn.Continue();
        }
    }

    @SpirePatch(clz = ApplyPowerAction.class, method = SpirePatch.CLASS)
    public static class PowerActionState {
        public static final SpireField<Boolean> checked = new SpireField<>(() -> false);
    }
}
