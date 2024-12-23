package spireCafe.interactables.patrons.dandadan;

import java.util.ArrayList;

import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.evacipated.cardcrawl.modthespire.patcher.PatchingException;
import com.megacrit.cardcrawl.rewards.RewardItem;
import com.megacrit.cardcrawl.screens.CombatRewardScreen;

import javassist.CannotCompileException;
import javassist.CtBehavior;

@SpirePatch2(clz = CombatRewardScreen.class, method = "setupItemReward")
public class RightballPotionPatch {
    public static RightballPotion rbp;

    @SpireInsertPatch(locator = Locator.class)
    public static SpireReturn<Void> returnPotionReward(ArrayList<RewardItem> ___rewards) {
        if (rbp == null) {
            return SpireReturn.Continue();
        }
        RewardItem potionReward = new RewardItem(rbp.makeCopy());
        ___rewards.add(potionReward);
        rbp = null;
        return SpireReturn.Continue();
    }

    static class Locator extends SpireInsertLocator {

        @Override
        public int[] Locate(CtBehavior ctBehavior) throws CannotCompileException, PatchingException {
            Matcher finalMatcher = new Matcher.MethodCallMatcher(CombatRewardScreen.class, "positionRewards");
            return LineFinder.findInOrder(ctBehavior, new ArrayList<Matcher>(), finalMatcher);
        }
    }
}
