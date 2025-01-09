package spireCafe.interactables.patrons.spikeslime;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.MeatOnTheBone;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class SpikeSlimeCutscene extends AbstractCutscene {
    public static final String ID = makeID(SpikeSlimeCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    public SpikeSlimeCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) {
            nextDialogue();
            this.dialog.addDialogOption(OPTIONS[0]+((SpikeSlimePatron)character).healAmount+OPTIONS[1], false, new HandfulOfSlime()).setOptionResult((i)->{
                character.alreadyPerformedTransaction = true;
                goToDialogue(2);
                character.setCutscenePortrait("Portrait2");
                character.blockingDialogueIndex = 0;
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, new HandfulOfSlime());
            });
            this.dialog.addDialogOption(OPTIONS[3], false, new HandfulOfSlime(HandfulOfSlime.BIG_SLIMY)).setOptionResult((i)-> {
                character.alreadyPerformedTransaction = true;
                goToDialogue(4);
                character.setCutscenePortrait("Portrait");
                character.blockingDialogueIndex = 1;
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, new HandfulOfSlime(HandfulOfSlime.BIG_SLIMY));
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float) Settings.WIDTH / 2.0F, (float)Settings.HEIGHT / 2.0F, new MeatOnTheBone());
            });
            this.dialog.addDialogOption(OPTIONS[4]).setOptionResult((i)-> {
                goToDialogue(6);
            });
        } else if (dialogueIndex == 2){ //the heal effect is delayed to the second button press to make sense for flavor
            nextDialogue();
            this.dialog.addDialogOption(OPTIONS[2]).setOptionResult((i)->{
                AbstractDungeon.player.heal(((SpikeSlimePatron)character).healAmount, true);
                endCutscene();
                this.dialog.clear();
            });
        } else if (dialogueIndex == 5 || dialogueIndex == 6) {
            endCutscene();
        } else {
            nextDialogue();
        }
    }
}