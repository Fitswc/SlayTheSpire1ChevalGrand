package org.chevalgrand.relics;

import basemod.abstracts.CustomRelic;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.ui.campfire.AbstractCampfireOption;
import java.util.ArrayList;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.restsiteoperations.ForgetOption;

public class ChevalGrandHat extends CustomRelic {
    public static final String ID = ChevalGrandSlayMod.id("ChevalGrandHat");
    private static final String ART = ChevalGrandSlayMod.IMAGES + "relics/";
    private boolean firstTurn;

    public ChevalGrandHat() {
        this(ID, "hat", RelicTier.STARTER);
    }

    protected ChevalGrandHat(String id, String art, RelicTier tier) {
        super(id, ImageMaster.loadImage(ART + art + ".png"), ImageMaster.loadImage(ART + art + "-outline.png"),
                tier, LandingSound.FLAT);
        largeImg = ImageMaster.loadImage(ART + art + "-large.png");
    }

    @Override
    public void addCampfireOption(ArrayList<AbstractCampfireOption> options) {
        if (options.stream().noneMatch(option -> option instanceof ForgetOption)) {
            options.add(new ForgetOption());
        }
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
