package spireCafe.interactables.attractions.makeup;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;

public class MakeupCutscene extends AbstractCutscene {
    public static final String ID = makeID(MakeupCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    public MakeupCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) { //Shows first dialogue text and then options on click while staying on the same dialogue
            //Do make up ayaya
            this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i)->{
                nextDialogue();
                character.alreadyPerformedTransaction = true;
                CardCrawlGame.sound.play("RELIC_DROP_MAGICAL");
                AbstractDungeon.effectList.add(new BorderFlashEffect(Color.PINK));
                MakeupTableAttraction.isAPrettySparklingPrincess = true;
            });

            //Be a party pooper
            this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i)->{
                goToDialogue(2);
            });
        } else {
            endCutscene();
        }
    }
}