package spireCafe.util.devcommands;

import java.util.ArrayList;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import spireCafe.CafeRoom;

public class CafeClear extends ConsoleCommand{

    public CafeClear() {
        maxExtraTokens = 1;
        minExtraTokens = 1;
        requiresPlayer = true;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        String cmd = tokens[2].toLowerCase();
        
        if (cmd.equals("all") || cmd.equals("attraction")){
            CafeRoom.devCommandAttraction = null;
        }

        if (cmd.equals("all") || cmd.equals("bartender")){
            CafeRoom.devCommandBartender = null;
        }
        
        if (cmd.equals("all") || cmd.equals("patron")){
            CafeRoom.devCommandPatrons = new String[CafeRoom.NUM_PATRONS];
        }
        
        if (cmd.equals("all") || cmd.equals("merchant")){
            CafeRoom.devCommandMerchant = null;
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();
        result.add("all");
        result.add("attraction");
        result.add("bartender");
        result.add("patron");
        result.add("merchant");
        return result;
    }

    @Override
    protected void errorMsg() {
        Cafe.cmdHelp();
    }
    
}
