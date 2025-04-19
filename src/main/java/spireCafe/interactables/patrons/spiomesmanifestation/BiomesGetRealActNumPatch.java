package spireCafe.interactables.patrons.spiomesmanifestation;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;

@SpirePatch(cls = "spireMapOverhaul.util.ActUtil", method = "getRealActNum", paramtypez = {}, requiredModId = "anniv6")
public class BiomesGetRealActNumPatch {
    public static boolean active = false;

    @SpirePostfixPatch
    public static int handleGetRealActNumInCafe(int __result) {
        return __result + (active ? 1 : 0);
    }
}
