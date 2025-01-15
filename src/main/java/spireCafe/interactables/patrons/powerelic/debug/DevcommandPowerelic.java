package spireCafe.interactables.patrons.powerelic.debug;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import basemod.devcommands.relic.*;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;

public class DevcommandPowerelic extends ConsoleCommand{

    public DevcommandPowerelic() {
        requiresPlayer = true;

        this.followup.put("standard", DevcommandPowerelicStandard.class);
        this.followup.put("customcost", DevcommandPowerelicCustom.class);
        this.followup.put("all", DevcommandPowerelicAll.class);
        this.followup.put("reliconly", DevcommandRelicToPower.class);
    }

    @Override
    protected void execute(String[] arg0, int arg1) {
        if(arg0.length>1 && !arg0[1].equals("standard")){
            errorMsg();
            return;
        }
        try {
            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), RelicLibrary.getRelic(PowerelicDebugRelic.ID).makeCopy());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void errorMsg() {
        cmdHelp();
    }

    public static void cmdHelp(){
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* {standard}");
        DevConsole.log("* customcost {cost}");
        DevConsole.log("* all");
        DevConsole.log("* reliconly {[id] | random {[amt]}}");
    }

}
