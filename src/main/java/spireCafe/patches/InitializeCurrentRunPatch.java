package spireCafe.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch2;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.Anniv7Mod;
import spireCafe.interactables.patrons.spiomesmanifestation.SpiomesManifestationPatron;

import java.util.HashSet;

@SpirePatch2(clz = AbstractDungeon.class, method = "generateSeeds")
public class InitializeCurrentRunPatch {
    @SpirePostfixPatch
    public static void initializeCurrentRun() {
        Anniv7Mod.currentRunSeenInteractables = new HashSet<>();
        SpiomesManifestationPatron.queuedBiomeID=null;
    }
}
