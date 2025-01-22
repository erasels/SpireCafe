package spireCafe.util.devcommands;

import basemod.BaseMod;
import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import spireCafe.screens.JukeboxScreen;

import java.util.ArrayList;

public class CafeJukebox extends ConsoleCommand {

    public CafeJukebox() {
        maxExtraTokens = 1; // Allow one additional token for the sub-command
        minExtraTokens = 1; // Require one additional token for the sub-command
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        String cmd = tokens[2].toLowerCase(); // Get the sub-command

        switch (cmd) {
            case "open":
                handleOpen();
                break;

            case "stop":
                handleStop();
                break;

            default:
                DevConsole.log("Unknown jukebox sub-command: " + cmd);
                errorMsg();
                break;
        }
    }

    private void handleOpen() {
        // Open the Jukebox Screen
        BaseMod.openCustomScreen(JukeboxScreen.ScreenEnum.JUKEBOX_SCREEN);
        DevConsole.log("Jukebox opened.");
    }

    private void handleStop() {
        if (JukeboxScreen.nowPlayingSong != null) {
            JukeboxScreen.stopCurrentMusic();
            CardCrawlGame.music.unsilenceBGM();
            DevConsole.log("Jukebox music stopped, restoring BGM");
        } else {
            DevConsole.log("No music is currently playing.");
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> options = new ArrayList<>();
        options.add("open");
        options.add("stop");
        return options;
    }

    @Override
    protected void errorMsg() {
        DevConsole.log("Usage:");
        DevConsole.log("* cafe jukebox open");
        DevConsole.log("* cafe jukebox stop");
    }
}
