package spireCafe.interactables.patrons.marisa;

import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class MarisaCutscene extends AbstractCutscene {
    public static final String ID = makeID(MarisaCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    public MarisaCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

}