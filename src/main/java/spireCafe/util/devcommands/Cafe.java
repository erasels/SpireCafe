package spireCafe.util.devcommands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;

public class Cafe extends ConsoleCommand{

    public Cafe() {
        followup.put("attraction", CafeAttraction.class);
        followup.put("bartender", CafeBartender.class);
        followup.put("patron", CafePatron.class);
        followup.put("merchant", CafeMerchant.class);
        followup.put("clear", CafeClear.class);
        followup.put("redecorate", CafeRedecorate.class);
        requiresPlayer = true;
    }

    @Override
    protected void execute(String[] arg0, int arg1) {
        cmdHelp();
    }

    @Override
    protected void errorMsg() {
        cmdHelp();
    }
    
    public static void cmdHelp() {
        DevConsole.couldNotParse();
        DevConsole.log("options are:");
        DevConsole.log("* attraction [id]");
        DevConsole.log("* bartender [id]");
        DevConsole.log("* patron [slot] [id]");
        DevConsole.log("* merchant [id]");
        DevConsole.log("* clear (all|attraction|bartender|patron|merchant)");
        DevConsole.log("* redecorate");
    }
}
