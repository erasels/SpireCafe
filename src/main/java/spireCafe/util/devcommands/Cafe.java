package spireCafe.util.devcommands;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;

public class Cafe extends ConsoleCommand{

    public Cafe() {
        followup.put("patron", CafePatron.class);
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
        DevConsole.log("* patron *[id]");
    }

}
