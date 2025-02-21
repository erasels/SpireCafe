package spireCafe.interactables.patrons.powerelic.implementation.debug;

import basemod.DevConsole;
import spireCafe.interactables.patrons.powerelic.implementation.ViolescentShard;

import java.util.ArrayList;

public class DevcommandPowerelicLock extends DevcommandPowerelic{
    public DevcommandPowerelicLock() {
        requiresPlayer = true;
        this.minExtraTokens = 0;
        this.maxExtraTokens = 0;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] arg0, int arg1) {
        if(!ViolescentShard.getOutfoxedStatus()){
            DevConsole.log("The second option at the Powerelic Professor was already locked!");
        }else{
            ViolescentShard.setOutfoxedStatus(false);
            DevConsole.log("The second option at the Powerelic Professor has been locked again.");
        }
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        complete=true;
        return result;
    }

}
