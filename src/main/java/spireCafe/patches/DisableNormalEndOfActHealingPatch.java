package spireCafe.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInstrumentPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import spireCafe.util.ActUtil;

// We want the healing to happen before the player enters the cafe, so we disable where it normally occurs and replicate
// the logic ourselves in CafeEntryExitPatch
@SpirePatch(clz = AbstractDungeon.class, method = "dungeonTransitionSetup")
public class DisableNormalEndOfActHealingPatch {
    @SpireInstrumentPatch
    public static ExprEditor disableNormalEndOfActHealing() {
        return new ExprEditor() {
            public void edit(MethodCall m) throws CannotCompileException {
                if (m.getClassName().equals(AbstractPlayer.class.getName()) && m.getMethodName().equals("heal"))
                    m.replace(String.format("{ if(%1$s.shouldHeal()) { $proceed($$); } }", DisableNormalEndOfActHealingPatch.class.getName()));
            }
        };
    }

    public static boolean shouldHeal() {
        int realActNum = ActUtil.getRealActNum();
        return realActNum != 2 && realActNum != 3;
    }
}
