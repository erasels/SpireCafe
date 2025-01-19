package spireCafe.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import javassist.CtBehavior;

public class PotencySaverPatch {

    @SpirePatch(clz = AbstractPotion.class, method = SpirePatch.CLASS)
    public static class PotionUseField {
        public static SpireField<Integer> isDepleted = new SpireField<>(() -> -1);
    }

    @SpirePatch(clz = AbstractPotion.class, method = "getPotency", paramtypez = {})
    public static class IncreasePotency {
        @SpireInsertPatch(locator = Locator.class, localvars = {"potency"})
        public static void patch(AbstractPotion __instance, @ByRef int[] potency) {
            if (PotionUseField.isDepleted.get(__instance) != -1) {
                potency[0] = PotionUseField.isDepleted.get(__instance);
            }
        }

        private static class Locator extends SpireInsertLocator {
            @Override
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.MethodCallMatcher(AbstractPlayer.class, "hasRelic");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }
}
