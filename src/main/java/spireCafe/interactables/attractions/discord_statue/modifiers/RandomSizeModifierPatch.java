package spireCafe.interactables.attractions.discord_statue.modifiers;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.helpers.CardModifierManager;

@SpirePatch2(clz = AbstractCard.class, paramtypez = { SpriteBatch.class }, method = "render")
public class RandomSizeModifierPatch {

    @SpirePrefixPatch
    public static void doubleDrawScale(AbstractCard __instance) {
        if (CardModifierManager.hasModifier(__instance, "anniv7:RandomSizeModifier"))
            __instance.drawScale *= ((RandomSizeModifier) (CardModifierManager
                    .getModifiers(__instance, "anniv7:RandomSizeModifier").get(0))).size;
    }

    @SpirePostfixPatch
    public static void setDrawScaleBack(AbstractCard __instance) {
        if (CardModifierManager.hasModifier(__instance, "anniv7:RandomSizeModifier"))
            __instance.drawScale /= ((RandomSizeModifier) (CardModifierManager
                    .getModifiers(__instance, "anniv7:RandomSizeModifier").get(0))).size;
    }
}
