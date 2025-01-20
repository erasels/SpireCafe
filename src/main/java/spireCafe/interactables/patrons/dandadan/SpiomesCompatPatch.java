package spireCafe.interactables.patrons.dandadan;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.rewards.RewardItem;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import spireMapOverhaul.patches.interfacePatches.RewardModifierPatches.ModifyRewardsPatch;

@SpirePatch2(clz = ModifyRewardsPatch.class, method = "ModifyRewards", requiredModId = "anniv6", optional = true)
public class SpiomesCompatPatch {

    public static RewardItem savedReward;

    public static boolean rewardContainsPotion() {
        for (RewardItem r : AbstractDungeon.combatRewardScreen.rewards) {
            if (r.type == RewardItem.RewardType.POTION && r.potion != null && r.potion instanceof RightballPotion) {
                return true;
            }
        }
        return false;
    }

    public static void preModifyRewards() {
        for (RewardItem r : AbstractDungeon.combatRewardScreen.rewards) {
            if (r.type == RewardItem.RewardType.POTION && r.potion != null && r.potion instanceof RightballPotion) {
                savedReward = r;
            }
        }
        if (savedReward != null) {
            AbstractDungeon.combatRewardScreen.rewards.remove(savedReward);
        }
    }

    public static void postModifyRewards() {
        AbstractDungeon.combatRewardScreen.rewards.add(savedReward);
        savedReward = null;
    }

    public static boolean rewardIsPotion(RewardItem r) {
        return r.type == RewardItem.RewardType.POTION && r.potion != null && r.potion instanceof RightballPotion;
    }

    @SpireInstrumentPatch
    public static ExprEditor Instrument() {
        return new PotionExprEditor();
    }

    public static class PotionExprEditor extends ExprEditor {
        @Override
        public void edit(MethodCall m) throws CannotCompileException {
            if (m.getMethodName().equals("modifyReward")) {
                m.replace("if (!" + SpiomesCompatPatch.class.getName() + ".rewardIsPotion($1)) {" +
                        "$_ = $proceed($$);" +
                        "}");
            } else if (m.getMethodName().equals("modifyRewards")) {
                m.replace(SpiomesCompatPatch.class.getName() + ".preModifyRewards();" +
                        "$_ = $proceed($$);" +
                        SpiomesCompatPatch.class.getName() + ".postModifyRewards();");

            }
        }
    }
}
