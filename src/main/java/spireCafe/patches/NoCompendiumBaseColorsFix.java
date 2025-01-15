//written by / stolen from Indi

package spireCafe.patches;

import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.NoCompendium;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import javassist.CannotCompileException;
import javassist.CtBehavior;


@SpirePatch2(clz = CardLibraryScreen.class, method = "initialize")
public class NoCompendiumBaseColorsFix {

    @SpireInsertPatch(locator = Locator.class)
    public static void Insert(CardGroup ___colorlessCards) {
        ___colorlessCards.group.removeIf(c -> c.getClass().isAnnotationPresent(NoCompendium.class));
    }

    private static class Locator extends SpireInsertLocator {
        @Override
        public int[] Locate(CtBehavior ctMethodToPatch) throws CannotCompileException, PatchingException { Matcher finalMatcher = new Matcher.MethodCallMatcher(CardLibraryScreen.class, "calculateScrollBounds");
            return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
        }
    }

}
