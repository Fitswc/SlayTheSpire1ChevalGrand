package org.chevalgrand.mechanics;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.screens.select.GridCardSelectScreen;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

/** A confirmable, cancelable grid selection outside the combat action queue. */
public final class DeckSelection {
    private final CurrentScreen origin;
    private final int limit;
    private final Consumer<List<AbstractCard>> complete;

    private DeckSelection(CurrentScreen origin, int limit, Consumer<List<AbstractCard>> complete) {
        this.origin = origin;
        this.limit = limit;
        this.complete = complete;
    }

    public static void open(CardGroup choices, int limit, String prompt, Consumer<List<AbstractCard>> complete) {
        GridCardSelectScreen grid = AbstractDungeon.gridSelectScreen;
        CurrentScreen origin = AbstractDungeon.screen;
        Field.selection.set(grid, new DeckSelection(origin, limit, complete));
        AbstractDungeon.previousScreen = origin == CurrentScreen.NONE ? null : origin;
        // A limit above the grid size prevents the native grid from auto-confirming.
        grid.open(choices, choices.size() + 1, true, prompt);
        grid.confirmButton.isDisabled = true;
        AbstractDungeon.overlayMenu.cancelButton.show(GridCardSelectScreen.TEXT[1]);
    }

    private void finish(GridCardSelectScreen grid, boolean confirmed) {
        Field.selection.set(grid, null);
        List<AbstractCard> selected = confirmed ? new ArrayList<>(grid.selectedCards) : Collections.emptyList();
        grid.selectedCards.forEach(AbstractCard::stopGlowing);
        grid.selectedCards.clear();
        if (origin == CurrentScreen.COMBAT_REWARD && AbstractDungeon.screen == CurrentScreen.NONE) {
            AbstractDungeon.combatRewardScreen.reopen();
        }
        complete.accept(selected);
    }

    private boolean returnedToOrigin() {
        return AbstractDungeon.screen == origin || AbstractDungeon.screen == CurrentScreen.NONE;
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = SpirePatch.CLASS)
    public static class Field {
        public static final SpireField<DeckSelection> selection = new SpireField<>(() -> null);
    }

    @SpirePatch(clz = GridCardSelectScreen.class, method = "update")
    public static class Confirm {
        public static void Postfix(GridCardSelectScreen __instance, @ByRef int[] ___cardSelectAmount) {
            DeckSelection selection = Field.selection.get(__instance);
            if (selection == null) return;
            if (selection.returnedToOrigin()) {
                selection.finish(__instance, true);
                return;
            }
            List<AbstractCard> cards = __instance.selectedCards;
            if (cards.size() > selection.limit) {
                cards.remove(cards.size() - 1).stopGlowing();
                ___cardSelectAmount[0]--;
            }
            __instance.confirmButton.isDisabled = cards.isEmpty();
        }
    }

    @SpirePatch(clz = CancelButton.class, method = "update")
    public static class Cancel {
        public static void Postfix() {
            GridCardSelectScreen grid = AbstractDungeon.gridSelectScreen;
            if (grid == null) return;
            DeckSelection selection = Field.selection.get(grid);
            if (selection != null && selection.returnedToOrigin()) {
                selection.finish(grid, false);
            }
        }
    }
}
