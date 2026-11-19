package org.chevalgrand.cards;

import basemod.abstracts.CustomCard;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.characters.ChevalGrandEnums;

abstract class StarterCard extends CustomCard {
    private final int damageUpgrade;
    private final int blockUpgrade;

    StarterCard(String id, String name, int cost, int damage, int block, int damageUpgrade,
                int blockUpgrade, CardType type, CardTarget target) {
        this(id, name, CardCrawlGame.languagePack.getCardStrings(id), cost, damage, block,
                damageUpgrade, blockUpgrade, type, target);
    }

    private StarterCard(String id, String name, CardStrings strings, int cost, int damage, int block,
                        int damageUpgrade, int blockUpgrade, CardType type, CardTarget target) {
        super(id, strings.NAME, ChevalGrandSlayMod.IMAGES + "cards/" + name + ".png", cost,
                strings.DESCRIPTION, type, ChevalGrandEnums.CARD_COLOR, CardRarity.BASIC, target);
        baseDamage = damage;
        baseBlock = block;
        this.damageUpgrade = damageUpgrade;
        this.blockUpgrade = blockUpgrade;
    }

    final void hit(AbstractPlayer player, AbstractMonster enemy) {
        addToBot(new DamageAction(enemy, new DamageInfo(player, damage, damageTypeForTurn), AttackEffect.SLASH_DIAGONAL));
    }

    final void gainBlock(AbstractPlayer player) {
        addToBot(new GainBlockAction(player, player, block));
    }

    @Override
    public final void upgrade() {
        if (!upgraded) {
            upgradeName();
            if (damageUpgrade > 0) {
                upgradeDamage(damageUpgrade);
            }
            if (blockUpgrade > 0) {
                upgradeBlock(blockUpgrade);
            }
        }
    }
}
