package org.chevalgrand.cards;

import com.megacrit.cardcrawl.actions.common.MakeTempCardInHandAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public final class LendReserve extends RaceCard {
    public LendReserve() {
        super("LendReserve", CardRarity.UNCOMMON, CardType.SKILL, CardTarget.SELF, 1, 4);
        initializeDescription();
    }

    @Override
    void onPlay(AbstractPlayer player, AbstractMonster enemy) {
        int slots = Settings.MAX_HAND_SIZE - player.hand.size() + (player.hand.contains(this) ? 1 : 0);
        if (slots > 0) addToBot(new MakeTempCardInHandAction(new Ball(), slots));
    }

    @Override
    public LendReserve makeCopy() {
        return new LendReserve();
    }
}
