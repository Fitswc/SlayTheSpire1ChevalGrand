package org.chevalgrand.cards;

import basemod.abstracts.CustomCard;
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
import com.megacrit.cardcrawl.powers.VulnerablePower;
import org.chevalgrand.ChevalGrandEnums;
import org.chevalgrand.ChevalGrandSlayMod;

abstract class RaceCard extends CustomCard {
    static final AttackEffect SLASH = AttackEffect.SLASH_DIAGONAL;

    final int maxStamina;
    RaceCard deckOrigin;
    int remaining;
    int restIndex;
    boolean preserveStamina;
    int nextAttackBonus;
    int costUpgrade;
    private int damageUpgrade;
    private int blockUpgrade;
    private int magicUpgrade;

    RaceCard(String name, CardRarity rarity, CardType type, CardTarget target, int cost, int stamina) {
        this(name, ChevalGrandSlayMod.id(name), rarity, type, target, cost, stamina);
    }

    private RaceCard(String name, String id, CardRarity rarity, CardType type, CardTarget target,
                     int cost, int stamina) {
        this(name, id, CardCrawlGame.languagePack.getCardStrings(id), rarity, type, target, cost, stamina);
    }

    private RaceCard(String name, String id, CardStrings strings, CardRarity rarity, CardType type,
                     CardTarget target, int cost, int stamina) {
        super(id, strings.NAME, ChevalGrandSlayMod.IMAGES + "cards/" + name + ".png", cost,
                strings.DESCRIPTION, type, type == CardType.STATUS ? CardColor.COLORLESS : ChevalGrandEnums.CARD_COLOR,
                rarity, target);
        maxStamina = remaining = stamina;
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

    final void setRemaining(int value) {
        remaining = Math.max(0, Math.min(maxStamina, value));
        if (deckOrigin != null) {
            deckOrigin.remaining = remaining;
        }
    }

    final void rememberRestOrder(int index) {
        restIndex = index;
        if (deckOrigin != null) {
            deckOrigin.restIndex = index;
        }
    }

    final void clearRestOrder() {
        rememberRestOrder(0);
    }

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
        if (maxStamina > 0) {
            if (Stamina.REST.remove(this)) {
                AbstractDungeon.player.drawPile.addToTop(this);
            }
            setRemaining(maxStamina);
            clearRestOrder();
        }
        String upgradedDescription = CardCrawlGame.languagePack.getCardStrings(cardID).UPGRADE_DESCRIPTION;
        if (upgradedDescription != null) rawDescription = upgradedDescription;
        initializeDescription();
    }

    @Override
    public final void use(AbstractPlayer player, AbstractMonster enemy) {
        if (maxStamina > 0) {
            if (!preserveStamina && !player.hasPower(RaceAbility.Effect.ENDLESS.id)) setRemaining(remaining - 1);
        }
        RaceAbility.beginAttack(this);
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

    final void apply(com.megacrit.cardcrawl.core.AbstractCreature target, RaceAbility.Effect effect, int amount) {
        addToBot(new ApplyPowerAction(target, AbstractDungeon.player, new RaceAbility(effect, target, amount)));
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
        java.util.List<AbstractCard> played = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        int size = played.size();
        if (size > 0 && played.get(size - 1) == this) {
            size--;
        }
        return (int) played.subList(0, size).stream().filter(card -> card.type == CardType.ATTACK).count();
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
