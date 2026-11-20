package org.chevalgrand.patches;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.scenes.AbstractScene;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;

@SpirePatch(clz = AbstractDungeon.class, method = "render", paramtypez = {SpriteBatch.class})
public final class CombatVisualPatches {
    @SpireInstrumentPatch
    public static ExprEditor replaceBackgroundRender() {
        return new ExprEditor() {
            @Override
            public void edit(MethodCall call) throws CannotCompileException {
                if (call.getClassName().equals(AbstractScene.class.getName()) && call.getMethodName().equals("renderCombatRoomBg")) {
                    call.replace("{ org.chevalgrand.visuals.DomainVisuals.renderBackground($0, $1); }");
                }
            }
        };
    }
}
