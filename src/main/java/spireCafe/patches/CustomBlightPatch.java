package spireCafe.patches;

import basemod.AutoAdd;
import com.evacipated.cardcrawl.modthespire.lib.*;
import com.megacrit.cardcrawl.blights.AbstractBlight;
import com.megacrit.cardcrawl.helpers.BlightHelper;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCBlight;

import java.util.ArrayList;

import static spireCafe.Anniv7Mod.modID;


public class CustomBlightPatch {
    private static final ArrayList<AbstractSCBlight> blights;

    static {
        blights = new ArrayList<>();
        new AutoAdd(modID)
                .packageFilter(Anniv7Mod.class)
                .any(AbstractSCBlight.class, (info, blight) -> {
                    if (!info.ignore) {
                        blights.add(blight);
                    }
                });
    }

    @SpirePatch(clz = BlightHelper.class, method = "getBlight")
    public static class GetBlightPatch {
        @SpirePrefixPatch
        public static SpireReturn<AbstractBlight> returnBlight(String ID) {
            for (AbstractSCBlight b : blights) {
                if (ID.equals(b.blightID)) {
                    return SpireReturn.Return(b.makeCopy());
                }
            }

            return SpireReturn.Continue();
        }
    }

    @SpirePatch2(clz = BlightHelper.class, method = "initialize")
    public static class InitBlight {
        @SpirePostfixPatch
        public static void patch() {
            for(AbstractSCBlight blight : blights) {
                BlightHelper.blights.add(blight.blightID);
                if(blight.isChestBlight) {
                    BlightHelper.chestBlights.add(blight.blightID);
                }
            }
        }
    }
}
