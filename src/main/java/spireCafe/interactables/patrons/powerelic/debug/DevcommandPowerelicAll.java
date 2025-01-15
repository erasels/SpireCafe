package spireCafe.interactables.patrons.powerelic.debug;

import basemod.DevConsole;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import spireCafe.interactables.patrons.powerelic.PowerelicCutscene;

import java.util.ArrayList;

public class DevcommandPowerelicAll extends DevcommandPowerelic{
    public DevcommandPowerelicAll() {
        requiresPlayer = true;
        this.minExtraTokens = 0;
        this.maxExtraTokens = 0;
        this.simpleCheck = true;
    }

    @Override
    protected void execute(String[] arg0, int arg1) {
        try {
            PowerelicCutscene.doTheVerySillyThing();
            DevConsole.log("Converted ALL powers to relics.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        complete=true;
        return result;
    }

    @Override
    public void errorMsg() {
        cmdHelp();
    }
}
