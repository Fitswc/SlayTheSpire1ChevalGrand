package org.chevalgrand.enchantments;

import basemod.abstracts.AbstractCardModifier;
import basemod.helpers.CardModifierManager;
import basemod.helpers.TooltipInfo;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;
import org.chevalgrand.ChevalGrandSlayMod;
import org.chevalgrand.keywords.RaceKeywords;
import org.chevalgrand.mechanics.Stamina;

public final class ReserveStepEnchantment extends AbstractCardModifier {
    private static final String ID = ChevalGrandSlayMod.id("ReserveStepEnchantment");
    private static final Set<AbstractCard> ENCHANTED = Collections.newSetFromMap(new IdentityHashMap<>());
    private static Texture icon;

    public static void loadArt() {
        icon = ImageMaster.loadImage(ChevalGrandSlayMod.IMAGES + "enchantments/reserve-step.png");
    }

    public static void apply(AbstractCard card) {
        CardModifierManager.addModifier(card, new ReserveStepEnchantment());
    }

    public static boolean preservesStamina(AbstractCard card) {
        return CardModifierManager.hasModifier(card, ID);
    }

    public static void clearCombat() {
        for (AbstractCard card : new ArrayList<>(ENCHANTED)) {
            CardModifierManager.removeModifiersById(card, ID, false);
        }
        ENCHANTED.clear();
    }

    @Override
    public String identifier(AbstractCard card) {
        return ID;
    }

    @Override
    public boolean shouldApply(AbstractCard card) {
        return Stamina.has(card, 1) && !preservesStamina(card);
    }

    @Override
    public void onInitialApplication(AbstractCard card) {
        ENCHANTED.add(card);
    }

    @Override
    public void onRemove(AbstractCard card) {
        ENCHANTED.remove(card);
    }

    public static TooltipInfo tooltip() {
        String[] text = RaceKeywords.text("ReserveStepEnchantment");
        return new TooltipInfo(text[0], text[1]);
    }

    @Override
    public List<TooltipInfo> additionalTooltips(AbstractCard card) {
        return Collections.singletonList(tooltip());
    }

    @Override
    public void onRender(AbstractCard card, SpriteBatch batch) {
        if (icon == null || card.isFlipped) return;
        float scale = Settings.scale * card.drawScale;
        float size = 36f * scale;
        Color previous = batch.getColor().cpy();
        batch.setColor(1f, 1f, 1f, card.transparency);
        batch.draw(icon, card.current_x + 106f * scale - size / 2f,
                card.current_y + 155f * scale - size / 2f, size, size);
        batch.setColor(previous);
    }

    @Override
    public AbstractCardModifier makeCopy() {
        return new ReserveStepEnchantment();
    }
}
