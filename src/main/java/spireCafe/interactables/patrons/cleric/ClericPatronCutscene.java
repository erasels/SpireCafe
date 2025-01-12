package spireCafe.interactables.patrons.cleric;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.exordium.Cleric;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class ClericPatronCutscene extends AbstractCutscene{

    public static final String ID = Anniv7Mod.makeID(ClericPatronCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    private ClericPatron cleric;

    public ClericPatronCutscene(ClericPatron character) {
        super(character, cutsceneStrings);
        this.cleric = (ClericPatron) character;
    }

    
    public void createOptionDialogue() {
        this.dialog.clear();
        this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i)->{
            character.alreadyPerformedTransaction = true;
            
        });
    }
}
