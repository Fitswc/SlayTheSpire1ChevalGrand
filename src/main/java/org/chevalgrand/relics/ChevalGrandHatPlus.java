package org.chevalgrand.relics;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import org.chevalgrand.ChevalGrandSlayMod;

public final class ChevalGrandHatPlus extends ChevalGrandHat {
    public static final String ID = ChevalGrandSlayMod.id("ChevalGrandHatPlus");

    public ChevalGrandHatPlus() {
        super(ID, "hat-plus", RelicTier.BOSS);
    }

    @Override
    public boolean canSpawn() {
        return AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(ChevalGrandHat.ID);
    }

    @Override
    public void obtain() {
        for (int i = 0; i < AbstractDungeon.player.relics.size(); i++) {
            if (ChevalGrandHat.ID.equals(AbstractDungeon.player.relics.get(i).relicId)) {
                instantObtain(AbstractDungeon.player, i, true);
                return;
            }
        }
        super.obtain();
    }

    @Override
    public ChevalGrandHatPlus makeCopy() {
        return new ChevalGrandHatPlus();
    }
}
