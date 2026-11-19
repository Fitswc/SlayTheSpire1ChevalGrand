package org.chevalgrand;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;

final class ChevalGrandHat extends CustomRelic {
    static final String ID = ChevalGrandSlayMod.id("ChevalGrandHat");
    private static final String ART = ChevalGrandSlayMod.IMAGES + "relics/";
    private boolean firstTurn;

    ChevalGrandHat() {
        super(ID, ImageMaster.loadImage(ART + "hat.png"), ImageMaster.loadImage(ART + "hat-outline.png"),
                RelicTier.STARTER, LandingSound.FLAT);
        largeImg = ImageMaster.loadImage(ART + "hat-large.png");
    }

    @Override
    public void atPreBattle() {
        firstTurn = true;
    }

    @Override
    public void atTurnStart() {
        if (firstTurn) {
            firstTurn = false;
            flash();
            addToTop(new GainEnergyAction(1));
            addToTop(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        }
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public ChevalGrandHat makeCopy() {
        return new ChevalGrandHat();
    }
}
