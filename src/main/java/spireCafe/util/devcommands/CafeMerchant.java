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
import spireCafe.abstracts.AbstractMerchant;

public class CafeMerchant extends ConsoleCommand{

    private static List<Class<? extends AbstractCafeInteractable>> getMerchants() {
        return Anniv7Mod.interactableClasses.entrySet().stream()
                .filter(entry -> AbstractMerchant.class.isAssignableFrom(entry.getValue()))
                .map(Map.Entry::getValue)
                .collect(Collectors.toList());
    }

    public CafeMerchant() {
        requiresPlayer = true;
        minExtraTokens = 1;
        maxExtraTokens = 1;
        simpleCheck = true;
    }

    @Override
    protected void execute(String[] tokens, int depth) {
        if (Anniv7Mod.interactableClasses.keySet().contains(tokens[2])) {
            CafeRoom.devCommandMerchant = tokens[2];
            DevConsole.log("Cafe merchant set to: " + tokens[2]);
        } else {
            DevConsole.log("Invalid merchant id");
        }
    }

    @Override
    protected ArrayList<String> extraOptions(String[] tokens, int depth) {
        ArrayList<String> result = new ArrayList<>();

        for (Class<? extends AbstractCafeInteractable> i : getMerchants()) {
            result.add(i.getSimpleName());
        }

        return result;
    }

    @Override
    protected void errorMsg() {
        Cafe.cmdHelp();
    }
}
