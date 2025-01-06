package spireCafe.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.audio.MusicMaster;
import spireCafe.screens.JukeboxScreen;

@SpirePatch(
        clz = MusicMaster.class,
        method = "playTempBgmInstantly",
        paramtypez = {String.class}
)
public class JukeboxOverridePatch {
    public static void Prefix(MusicMaster __instance, String key) {
        // Check if overrides are enabled
        if (JukeboxScreen.overrideEnabled) {
            // Prevent SHOP music from interrupting if the override is active
            if (JukeboxScreen.shopOverride && "SHOP".equals(key)) {
                System.out.println("Jukebox override active: Preventing SHOP music interruption.");
                return; // Do not proceed with playing SHOP music
            }

            // Prevent ELITE music from interrupting if the override is active
            if (JukeboxScreen.eliteOverride && "ELITE".equals(key)) {
                System.out.println("Jukebox override active: Preventing ELITE music interruption.");
                return; // Do not proceed with playing ELITE music
            }

            // Prevent SHRINE music from interrupting if the override is active
            if (JukeboxScreen.shrineOverride && "SHRINE".equals(key)) {
                System.out.println("Jukebox override active: Preventing SHRINE music interruption.");
                return; // Do not proceed with playing SHRINE music
            }

            // Prevent BOSS music from interrupting if the override is active
            if (JukeboxScreen.bossOverride && (key.startsWith("BOSS_") || "MINDBLOOM".equals(key))) {
                System.out.println("Jukebox override active: Preventing BOSS music interruption.");
                return; // Do not proceed with playing BOSS music
            }

            // Prevent EVENT music from interrupting if the override is active
            if (JukeboxScreen.eventOverride && "EVENT".equals(key)) {
                System.out.println("Jukebox override active: Preventing EVENT music interruption.");
                return; // Do not proceed with playing EVENT music
            }
        }
    }
}
