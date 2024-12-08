package spireCafe.util.devcommands;

import java.util.ArrayList;

import basemod.devcommands.ConsoleCommand;

public class CafeNPC extends ConsoleCommand{

    public CafeNPC() {
        maxExtraTokens = 2;
        minExtraTokens = 2;

    }

    @Override
    protected void execute(String[] tokens, int depth) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'execute'");
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();

        int slots = 3; // TODO: Grab this value dynamically.
        for (int i = 0; i < slots; i++){
            result.add(String.valueOf(i));
        }

        return result;
    }

}
