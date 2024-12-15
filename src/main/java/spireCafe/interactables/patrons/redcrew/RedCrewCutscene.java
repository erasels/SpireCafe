package spireCafe.interactables.patrons.redcrew;

import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class RedCrewCutscene extends AbstractCutscene {
    public static final String ID = makeID(RedCrewCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    public RedCrewCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

}