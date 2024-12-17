package spireCafe.util.devcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.abstracts.AbstractAttraction;
import spireCafe.abstracts.AbstractCafeInteractable;

public class CafeAttraction extends ConsoleCommand{

    private static List<Class<? extends AbstractCafeInteractable>> getAttractions() {
        return Anniv7Mod.interactableClasses.entrySet().stream()
                .filter(entry -> AbstractAttraction.class.isAssignableFrom(entry.getValue()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public CafeAttraction() {
        requiresPlayer = true;
        minExtraTokens = 1;
        maxExtraTokens = 1;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (Anniv7Mod.interactableClasses.keySet().contains(tokens[2])) {
            CafeRoom.devCommandAttraction = tokens[2];
            DevConsole.log("Cafe attraction set to: " + tokens[2]);
        } else {
            DevConsole.log("Invalid attraction id");
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();

        for (Class<? extends AbstractCafeInteractable> i : getAttractions()) {
            result.add(i.getSimpleName());
        }

        return result;
    }

    @Override
    protected void errorMsg() {
        Cafe.cmdHelp();
    }
}
