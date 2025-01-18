package spireCafe.patches;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.rooms.RestRoom;

import spireCafe.screens.JukeboxScreen;

@SpirePatch(clz = RestRoom.class, method = "fadeIn")
public class CampfireExitPatch {
    @SpirePrefixPatch
    public static SpireReturn<Void> checkJukebox(RestRoom instance) {
        // If the Jukebox is playing, skip the unsilenceBGM call
        if (JukeboxScreen.isPlaying) {
              return SpireReturn.Return();
        }

        // Allow the original method to execute if Jukebox is not playing
        return SpireReturn.Continue();
    }
}
