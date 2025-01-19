package spireCafe.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpireReturn;
import com.megacrit.cardcrawl.rooms.MonsterRoomBoss;
import spireCafe.screens.JukeboxScreen;
@SpirePatch(
        clz = MonsterRoomBoss.class,
        method = "onPlayerEntry"
)
public class MonsterRoomBossMusicPatch {
    //Pauses the Jukebox for the Boss music
    @SpireInsertPatch(rloc = 4)
    public static SpireReturn<Void> stopJukeboxMusic(MonsterRoomBoss __instance) {
        if (JukeboxScreen.isPlaying) {
            JukeboxScreen.stopCurrentMusic();
        }
        return SpireReturn.Continue();
    }
}