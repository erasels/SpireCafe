package spireCafe.patches;

import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import javassist.CtBehavior;

import java.util.ArrayList;

public class CardsDrawnDuringTurnPatch {
    public static ArrayList<AbstractCard> CARDS_DRAWN = new ArrayList<>();
    
    @SpirePatch(
        clz = AbstractPlayer.class,
        method = "draw",
        paramtypez = {int.class}
    )
    public static class OnDrawCardPatch {
        @SpireInsertPatch(
            locator = Locator.class,
            localvars = {"c"}
        )
        public static void onDraw(AbstractPlayer __instance, AbstractCard c) {
            CARDS_DRAWN.add(c);
        }
        private static class Locator extends SpireInsertLocator {
            public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
                Matcher finalMatcher = new Matcher.FieldAccessMatcher(AbstractPlayer.class, "powers");
                return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
            }
        }
    }

    @SpirePatch(
            clz = AbstractPlayer.class,
            method = "applyStartOfTurnRelics"
    )
    public static class AbstractPlayerApplyStartOfTurnRelicsPatch {
        public static void Prefix(AbstractPlayer __instance) {
            CARDS_DRAWN.clear();
        }
    }
}
