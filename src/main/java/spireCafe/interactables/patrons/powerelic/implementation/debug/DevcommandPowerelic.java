package spireCafe.interactables.patrons.powerelic.implementation.debug;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import spireCafe.interactables.patrons.powerelic.implementation.ViolescentShard;

public class DevcommandPowerelic extends ConsoleCommand{

    public DevcommandPowerelic() {
        this.minExtraTokens = 1;

        requiresPlayer = true;
//        this.followup.put("standard", DevcommandPowerelicStandard.class);
//        this.followup.put("customcost", DevcommandPowerelicCustom.class);
        this.followup.put("all", DevcommandPowerelicAll.class);
        this.followup.put("reliconly", DevcommandRelicToPower.class);
        this.followup.put("lock", DevcommandPowerelicLock.class);
    }

    @Override
    protected void execute(String[] arg0, int arg1) {
        outfoxedCheck();

        if(arg0.length>1 && !arg0[1].equals("standard")){
            errorMsg();
            return;
        }
//        try {
//            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), RelicLibrary.getRelic(PowerelicDebugRelic.ID).makeCopy());
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public void errorMsg() {
        cmdHelp();
    }

    public static void cmdHelp(){
        outfoxedCheck();
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
//        DevConsole.log("* {standard}");
//        DevConsole.log("* customcost {cost}");
        DevConsole.log("* all");
        DevConsole.log("* reliconly {[id] | random {[amt]}}");
    }

    public static void outfoxedCheck(){
        if(!ViolescentShard.getOutfoxedStatus()){
            DevConsole.log("The second option at the Powerelic Professor has been unlocked.");
            ViolescentShard.setOutfoxedStatus(true);
        }
    }

}
