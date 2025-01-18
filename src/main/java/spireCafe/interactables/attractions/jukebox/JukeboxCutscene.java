package spireCafe.interactables.attractions.jukebox;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.BorderFlashEffect;

import basemod.animations.SpriterAnimation;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.screens.JukeboxScreen;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class JukeboxCutscene extends AbstractCutscene {
    public static final String ID = Anniv7Mod.makeID(JukeboxCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    public JukeboxCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) {
            this.dialog.addDialogOption("Look at the buttons").setOptionResult((i) -> {
                BaseMod.openCustomScreen(JukeboxScreen.ScreenEnum.JUKEBOX_SCREEN); // Open the custom screen
            });

            this.dialog.addDialogOption("Back").setOptionResult((i) -> {
                goToDialogue(2); // End interaction
            });
        } else {
            endCutscene(); // End the cutscene
        }
    }
}