//package spireCafe.interactables.patrons.powerelic.debug;
//
//import basemod.devcommands.ConsoleCommand;
//import basemod.devcommands.relic.Relic;
//import basemod.helpers.ConvertHelper;
//import com.megacrit.cardcrawl.core.Settings;
//import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
//import com.megacrit.cardcrawl.helpers.RelicLibrary;
//import com.megacrit.cardcrawl.relics.AbstractRelic;
//import spireCafe.CafeRoom;
//import spireCafe.abstracts.AbstractCafeInteractable;
//import spireCafe.interactables.patrons.powerelic.PowerelicAllowlist;
//import spireCafe.interactables.patrons.powerelic.PowerelicCutscene;
//import spireCafe.util.Wiz;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//
//public class DevcommandPowerelicCustom extends ConsoleCommand {
//
//    public DevcommandPowerelicCustom() {
//        requiresPlayer = true;
//        this.minExtraTokens = 1;
//        this.maxExtraTokens = 2;
//        this.simpleCheck = false;
//    }
//
//    public void execute(String[] tokens, int depth) {
//        int countIndex = 1;
//        int count = 1;
//        if (tokens.length > countIndex + 1 && ConvertHelper.tryParseInt(tokens[countIndex + 1]) != null) {
//            count = ConvertHelper.tryParseInt(tokens[countIndex + 1], 0);
//        }
//        try {
//            AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), ((PowerelicDebugRelic)RelicLibrary.getRelic(PowerelicDebugRelic.ID)).makeCopy(count));
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public ArrayList<String> extraOptions(String[] tokens, int depth) {
//        if (tokens[depth].matches("\\d+")) {
//            complete = true;
//        }
//
//        return ConsoleCommand.smallNumbers();
//    }
//
//    @Override
//    public void errorMsg() {
//        DevcommandPowerelic.cmdHelp();
//    }
//}
