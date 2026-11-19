package org.chevalgrand.cards;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import org.chevalgrand.ChevalGrandSlayMod;

final class RacePower extends AbstractPower {
    enum Effect { FlowingAssault, UnwaveringStep, WinningRhythm, RemainingUsesSense, OldRoadWarmth }

    private final Effect effect;

    RacePower(Effect effect, AbstractPlayer player, int amount) {
        this.effect = effect;
        ID = ChevalGrandSlayMod.id(effect.name());
        name = CardCrawlGame.languagePack.getCardStrings(ID).NAME;
        owner = player;
        this.amount = amount;
        type = PowerType.BUFF;
        String imagePath = ChevalGrandSlayMod.IMAGES + "powers/" + effect.name();
        region48 = region(imagePath + ".png");
        region128 = region(imagePath + "-large.png");
        updateDescription();
    }

    private TextureAtlas.AtlasRegion region(String path) {
        Texture image = ImageMaster.loadImage(path);
        return new TextureAtlas.AtlasRegion(image, 0, 0, image.getWidth(), image.getHeight());
    }

    @Override
    public void updateDescription() {
        description = CardCrawlGame.languagePack.getCardStrings(ID).DESCRIPTION.replace("!M!", Integer.toString(amount));
    }

    @Override
    public void onAfterUseCard(AbstractCard card, UseCardAction action) {
        if (AbstractDungeon.actionManager.turnHasEnded) {
            return;
        }
        if (effect == Effect.WinningRhythm && card.type == AbstractCard.CardType.SKILL) {
            flash();
            addToBot(new GainBlockAction(owner, owner, amount));
        }
        if (card.type != AbstractCard.CardType.ATTACK) return;
        if (effect == Effect.UnwaveringStep && card.cost >= 1) {
            flash();
            addToBot(new GainBlockAction(owner, owner, amount));
        } else if (effect == Effect.FlowingAssault && AbstractDungeon.actionManager.cardsPlayedThisTurn.stream()
                .filter(played -> played.type == AbstractCard.CardType.ATTACK).count() > 3) {
            flash();
            addToBot(new DrawCardAction((AbstractPlayer) owner, amount));
        }
    }

    void onCardRested() {
        if (effect == Effect.OldRoadWarmth) {
            addToBot(new GainBlockAction(owner, owner, amount));
        } else if (effect == Effect.RemainingUsesSense && !AbstractDungeon.actionManager.turnHasEnded) {
            AbstractMonster target = AbstractDungeon.getMonsters().getRandomMonster(null, true, AbstractDungeon.cardRandomRng);
            if (target != null) {
                addToBot(new DamageAction(target, new DamageInfo(owner, amount, DamageInfo.DamageType.THORNS),
                        AbstractGameAction.AttackEffect.SLASH_DIAGONAL));
            }
        }
    }
}
