package org.chevalgrand.mechanics;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import java.util.List;

public final class AttackChain {
    private AttackChain() {}

    public static int countThisTurn() {
        return countBefore(null);
    }

    public static int countBefore(AbstractCard currentPlay) {
        List<AbstractCard> played = AbstractDungeon.actionManager.cardsPlayedThisTurn;
        int size = played.size();
        if (currentPlay != null && size > 0 && played.get(size - 1) == currentPlay) size--;
        int count = 0;
        for (int i = 0; i < size; i++) {
            if (played.get(i).type == AbstractCard.CardType.ATTACK) count++;
        }
        return count;
    }
}
