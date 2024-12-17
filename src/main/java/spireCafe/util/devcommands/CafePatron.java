package spireCafe.util.devcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.abstracts.AbstractPatron;

public class CafePatron extends ConsoleCommand{

    private static List<Class<? extends AbstractCafeInteractable>> getPatrons() {
        return Anniv7Mod.interactableClasses.entrySet().stream()
                .filter(entry -> AbstractPatron.class.isAssignableFrom(entry.getValue()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public CafePatron() {
        maxExtraTokens = 2;
        minExtraTokens = 2;
        requiresPlayer = true;
    }
    
    @Override
    protected void execute(String[] tokens, int depth) {
        int i;
        try {
            i = Integer.parseInt(tokens[2]);
        } catch (Exception e) {
            DevConsole.log("Invalid Patron Slot");
            return;
        }

        if (i >= CafeRoom.NUM_PATRONS || i < 0) {
            DevConsole.log("Invalid Patron Slot");
        }

        if (Anniv7Mod.interactableClasses.keySet().contains(tokens[3])){
            CafeRoom.devCommandPatrons[i] = tokens[3];
            DevConsole.log("Cafe patron slot " + i + " set to: " + tokens[3]);
        } else {
            DevConsole.log("Invalid patron id");
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();

        int slots = CafeRoom.NUM_PATRONS;
        for (int i = 0; i < slots; i++){
            result.add(String.valueOf(i));
        }

        if (result.contains(tokens[depth]) && tokens.length > depth + 1){
            result.clear();
            for (Class<? extends AbstractCafeInteractable> i : getPatrons()){
                result.add(i.getSimpleName());
            }
            if (result.contains(tokens[depth + 1])){
                complete = true;
            }
        }

        return result;
    }

    @Override
    protected void errorMsg() {
        Cafe.cmdHelp();
    }

}
