package org.chevalgrand.powers;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import java.util.HashMap;
import java.util.Map;
import org.chevalgrand.ChevalGrandSlayMod;

final class RacePowerArt {
    private static final Map<String, TextureAtlas.AtlasRegion> REGIONS = new HashMap<>();

    private RacePowerArt() {}

    static TextureAtlas.AtlasRegion region(String name) {
        return REGIONS.computeIfAbsent(name, key -> {
            Texture texture = ImageMaster.loadImage(ChevalGrandSlayMod.IMAGES + "powers/" + key + ".png");
            return new TextureAtlas.AtlasRegion(texture, 0, 0, texture.getWidth(), texture.getHeight());
        });
    }
}
