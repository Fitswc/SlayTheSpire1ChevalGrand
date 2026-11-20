package org.chevalgrand.cards;

import basemod.abstracts.CustomCard;
import basemod.helpers.TooltipInfo;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.VulnerablePower;
import java.util.List;
import java.util.regex.Pattern;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.characters.ChevalGrandEnums;
import org.chevalgrand.enchantments.ReserveStepEnchantment;
import org.chevalgrand.keywords.RaceKeywords;
import org.chevalgrand.mechanics.AttackChain;
import org.chevalgrand.mechanics.CardMoves;
import org.chevalgrand.mechanics.Stamina;
import org.chevalgrand.powers.EndlessStaminaPower;

public abstract class RaceCard extends CustomCard {
    public static final AttackEffect SLASH = AttackEffect.SLASH_DIAGONAL;
    private static final String STAMINA_MARKER = "!" + ChevalGrandSlayMod.id("Stamina") + "!";
    private static final Pattern STAMINA_LINE = Pattern.compile(Pattern.quote(STAMINA_MARKER) + "/\\d+");

    private final int baseStamina;
    private int combatMaximum;
    private boolean hatBonus;
    RaceCard deckOrigin;
    int remaining;
    int restIndex;
    int nextAttackBonus;
    int costUpgrade;
    private int damageUpgrade;
    private int blockUpgrade;
    private int magicUpgrade;

    RaceCard(String name, CardRarity rarity, CardType type, CardTarget target, int cost, int stamina) {
        this(name, name, rarity, type, target, cost, stamina);
    }

    RaceCard(String name, String portrait, CardRarity rarity, CardType type, CardTarget target,
             int cost, int stamina) {
        this(portrait, ChevalGrandSlayMod.id(name), CardCrawlGame.languagePack.getCardStrings(ChevalGrandSlayMod.id(name)),
                rarity, type, target, cost, stamina);
    }

    private RaceCard(String name, String id, CardStrings strings, CardRarity rarity, CardType type,
                     CardTarget target, int cost, int stamina) {
        super(id, strings.NAME, ChevalGrandSlayMod.IMAGES + "cards/" + name + ".png", cost,
                strings.DESCRIPTION, type, type == CardType.STATUS ? CardColor.COLORLESS : ChevalGrandEnums.CARD_COLOR,
                rarity, target);
        baseStamina = combatMaximum = remaining = stamina;
        baseMagicNumber = magicNumber = 0;
        isMultiDamage = target == CardTarget.ALL_ENEMY && type == CardType.ATTACK;
    }

    final void configureDamage(int amount, int upgrade) {
        baseDamage = damage = amount;
        damageUpgrade = upgrade;
    }

    final void configureBlock(int amount, int upgrade) {
        baseBlock = block = amount;
        blockUpgrade = upgrade;
    }

    final void configureMagic(int amount, int upgrade) {
        baseMagicNumber = magicNumber = amount;
        magicUpgrade = upgrade;
    }

    public final void setRemaining(int value) {
        remaining = Math.max(0, Math.min(maxStamina(), value));
        syncDeckStamina();
    }

    private void syncDeckStamina() {
        if (deckOrigin != null) {
            int spent = Math.max(0, maxStamina() - remaining);
            deckOrigin.setRemaining(deckOrigin.maxStamina() - spent);
        }
    }

    public final void rememberRestOrder(int index) {
        restIndex = index;
        if (deckOrigin != null) {
            deckOrigin.restIndex = index;
        }
    }

    public final void clearRestOrder() {
        rememberRestOrder(0);
    }

    public final int maxStamina() { return combatMaximum; }

    public final int baseStamina() { return baseStamina; }

    public final void applyHatBonus() {
        if (baseStamina == 0 || hatBonus) return;
        hatBonus = true;
        combatMaximum++;
        remaining++;
        refreshStaminaDescription();
    }

    public final void fillCombatStamina(int maximum) {
        combatMaximum = remaining = maximum;
        refreshStaminaDescription();
    }

    public final void copyCombatStamina(RaceCard original) {
        combatMaximum = original.combatMaximum;
        remaining = original.remaining;
        hatBonus = original.hatBonus;
        if (original.selfRetain && !selfRetain) grantCombatRetain();
        refreshStaminaDescription();
    }

    public final void grantCombatRetain() {
        if (selfRetain) return;
        selfRetain = true;
        rawDescription = RaceKeywords.text("Potential")[1] + " NL " + rawDescription;
        initializeDescription();
    }

    public final int remainingStamina() { return remaining; }

    public final RaceCard deckOrigin() { return deckOrigin; }

    public final void linkDeckOrigin(RaceCard original) { deckOrigin = original; }

    public final int restOrder() { return restIndex; }

    public final void gainBonusStamina() {
        remaining++;
        syncDeckStamina();
    }

    public final void clearAttackBonus() { nextAttackBonus = 0; }

    @Override
    public boolean canUpgrade() {
        return !upgraded && (costUpgrade != 0 || damageUpgrade != 0 || blockUpgrade != 0 || magicUpgrade != 0);
    }

    @Override
    public final void upgrade() {
        if (!canUpgrade()) {
            return;
        }
        upgradeName();
        if (costUpgrade != 0) upgradeBaseCost(cost + costUpgrade);
        if (damageUpgrade != 0) upgradeDamage(damageUpgrade);
        if (blockUpgrade != 0) upgradeBlock(blockUpgrade);
        if (magicUpgrade != 0) upgradeMagicNumber(magicUpgrade);
        if (maxStamina() > 0) {
            Stamina.restoreAfterUpgrade(this);
        }
        String upgradedDescription = CardCrawlGame.languagePack.getCardStrings(cardID).UPGRADE_DESCRIPTION;
        if (upgradedDescription != null) {
            rawDescription = selfRetain ? RaceKeywords.text("Potential")[1] + " NL " + upgradedDescription : upgradedDescription;
            rawDescription = withCurrentMaximum(rawDescription);
        }
        initializeDescription();
    }

    @Override
    public final void use(AbstractPlayer player, AbstractMonster enemy) {
        if (maxStamina() > 0) {
            if (!ReserveStepEnchantment.preservesStamina(this) && !player.hasPower(EndlessStaminaPower.POWER_ID)) {
                setRemaining(remaining - 1);
            }
        }
        CardMoves.beginAttack(this);
        onPlay(player, enemy);
    }

    @Override
    public void applyPowers() {
        int original = baseDamage;
        baseDamage += nextAttackBonus;
        super.applyPowers();
        baseDamage = original;
        if (nextAttackBonus > 0) isDamageModified = true;
    }

    @Override
    public void calculateCardDamage(AbstractMonster enemy) {
        int original = baseDamage;
        baseDamage += nextAttackBonus;
        super.calculateCardDamage(enemy);
        baseDamage = original;
        if (nextAttackBonus > 0) isDamageModified = true;
    }

    abstract void onPlay(AbstractPlayer player, AbstractMonster enemy);

    @Override
    public List<TooltipInfo> getCustomTooltips() {
        return RaceKeywords.cardTips(baseStamina > 0, rawDescription);
    }

    private void refreshStaminaDescription() {
        String updated = withCurrentMaximum(rawDescription);
        if (!updated.equals(rawDescription)) {
            rawDescription = updated;
            initializeDescription();
        }
    }

    private String withCurrentMaximum(String description) {
        return STAMINA_LINE.matcher(description).replaceAll(STAMINA_MARKER + "/" + maxStamina());
    }

    final void apply(AbstractPower power) {
        addToBot(new ApplyPowerAction(power.owner, AbstractDungeon.player, power));
    }

    final int xEnergy(AbstractPlayer player) {
        int energy = energyOnUse;
        if (player.hasRelic("Chemical X")) {
            player.getRelic("Chemical X").flash();
            energy += 2;
        }
        if (!freeToPlayOnce) player.energy.use(energyOnUse);
        return energy;
    }

    final int previousAttacks() {
        return AttackChain.countBefore(this);
    }

    final void hit(AbstractPlayer player, AbstractMonster enemy, int amount) {
        addToBot(new DamageAction(enemy, new DamageInfo(player, amount, damageTypeForTurn), SLASH));
    }

    final void vulnerable(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new ApplyPowerAction(enemy, player, new VulnerablePower(enemy, magicNumber, false)));
    }

    final void gainBlock(AbstractPlayer player, int amount) {
        if (amount > 0) addToBot(new GainBlockAction(player, player, amount));
    }

    final void draw(AbstractPlayer player, int amount) {
        if (amount > 0) addToBot(new DrawCardAction(player, amount));
    }
}
