package spireCafe.util.devcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import basemod.DevConsole;
import basemod.devcommands.ConsoleCommand;
import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.abstracts.AbstractCafeInteractable;

public class CafeBartender extends ConsoleCommand{

    private static List<Class<? extends AbstractCafeInteractable>> getBartenders() {
        return Anniv7Mod.interactableClasses.entrySet().stream()
                .filter(entry -> AbstractBartender.class.isAssignableFrom(entry.getValue()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public CafeBartender() {
        requiresPlayer = true;
        minExtraTokens = 1;
        maxExtraTokens = 1;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (Anniv7Mod.interactableClasses.keySet().contains(tokens[2])) {
            CafeRoom.devCommandBartender = tokens[2];
            DevConsole.log("Cafe bartender set to: " + tokens[2]);
        } else {
            DevConsole.log("Invalid bartender id");
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();

        for (Class<? extends AbstractCafeInteractable> i : getBartenders()) {
            result.add(i.getSimpleName());
        }

        return result;
    }

    @Override
    protected void errorMsg() {
        Cafe.cmdHelp();
    }
    
}
