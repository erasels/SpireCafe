package spireCafe.interactables.patrons.powerelic.debug;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;

import java.util.ArrayList;

public class DevcommandPowerelicStandard extends DevcommandPowerelic{

    public DevcommandPowerelicStandard() {
        requiresPlayer = true;
        this.minExtraTokens = 0;
        this.maxExtraTokens = 0;
        this.simpleCheck = true;
    }

    protected void execute(String[] arg0, int arg1) {
        DevConsole.log("\"standard\" is optional.  You can just type \"powerelic\" by itself.");
        super.execute(arg0,arg1);
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        complete=true;
        return result;
    }

    @Override
    public void errorMsg() {
        DevcommandPowerelic.cmdHelp();
    }

}
