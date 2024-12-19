package spireCafe.interactables.patrons.redlouse;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class RedLouseCutscene extends AbstractCutscene {
    public static final String ID = makeID(RedLouseCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    private boolean curlUpOnFinish = false;
    public RedLouseCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) {
            nextDialogue();
            this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i)->{
                character.alreadyPerformedTransaction = true;
                goToDialogue(2);
                character.blockingDialogueIndex = 0;
                curlUpOnFinish = true;
                character.setCutscenePortrait("Portrait2");
                CardCrawlGame.sound.play("ATTACK_PIERCING_WAIL", 0f);
            });
            this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i)-> goToDialogue(3));
        } else if (dialogueIndex >= 2) {
            endCutscene();
            if(curlUpOnFinish){
                ((RedLousePatron)character).curlUp();
            }
        } else {
            nextDialogue();
        }
    }
}