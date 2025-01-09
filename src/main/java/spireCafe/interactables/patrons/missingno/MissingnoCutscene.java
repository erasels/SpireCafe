package spireCafe.interactables.patrons.missingno;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.localization.LocalizedStrings;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.ui.Dialog;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.Anniv7Mod.modID;
import static spireCafe.interactables.patrons.missingno.MarkovChain.MarkovType.MISSINGNO;


public class MissingnoCutscene extends AbstractCutscene {
    public static final String ID = makeID(MissingnoCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    public static BitmapFont font;
    private static final FileHandle FONT_FILE = Gdx.files.internal(modID + "Resources/fonts/missingno/Gridlockd.ttf");

    // When localization is implemented, this will need to be modified to use some sort of default text
    // Either that or they can be super cool and generate their own Markov text in whatever language

    public MissingnoCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
        if (font == null) {
            setupFont();
        }
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) {
            nextDialogue();
            this.dialog.addDialogOption(OPTIONS[0]).setOptionResult((i)->{
                character.alreadyPerformedTransaction = true;
                AbstractDungeon.getCurrRoom().spawnRelicAndObtain((float)(Settings.WIDTH / 2), (float)(Settings.HEIGHT / 2), new MissingnoRelic());
                nextDialogue();
            });
            this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i)->{
                goToDialogue(3);
            });
        } else if (dialogueIndex == 3) {
            endCutscene();
        } else {
            nextDialogue();
        }
    }

    @Override
    protected void goToDialogue(int newIndex) {
        Dialog.optionList.clear();
        this.dialogueIndex = newIndex;
        if (this.dialogueIndex != 3) {
            updateDialogueText();
        } else {
            endCutscene();
        }
    }

    @Override
    protected void updateDialogueText() {
        String text = MarkovChain.getInstance(MISSINGNO).generateText();
        if(dialogueIndex == 0) {
            String speakerName = FontHelper.colorString(character.name, "y") + " NL ";
            text = speakerName + text;
        }
        if(dialogueIndex == 1) {
            text = text + "?";
        }
        if (this.show) {
            this.show = false;
            this.dialog.show(text);
        } else {
            this.dialog.updateBodyText(text);
        }
    }

    @Override
    public String getBlockingDialogue() {
        String text = MarkovChain.getInstance(MISSINGNO).generateText();
        String speakerName = FontHelper.colorString(character.name, "y") + " NL ";
        return speakerName + text;
    }

    @Override
    protected void showBlockingDialogue() {
        String text = getBlockingDialogue();
        if (this.show) {
            this.show = false;
            this.dialog.show(text);
        } else {
            this.dialog.updateBodyText(text);
        }
    }

    private static void setupFont() {
        FreeTypeFontGenerator g = new FreeTypeFontGenerator(FONT_FILE);
        FreeTypeFontGenerator.FreeTypeFontParameter p = new FreeTypeFontGenerator.FreeTypeFontParameter();
        p.characters = "";
        p.incremental = true;
        p.size = Math.round(50.0f * Settings.scale);
        p.gamma = 1.2F;
        p.minFilter = Texture.TextureFilter.Linear;
        p.magFilter = Texture.TextureFilter.Linear;
        g.scaleForPixelHeight(p.size);
        font = g.generateFont(p);
        font.setUseIntegerPositions(false);
        (font.getData()).markupEnabled = true;
        if (LocalizedStrings.break_chars != null)
            (font.getData()).breakChars = LocalizedStrings.break_chars.toCharArray();
        (font.getData()).fontFile = FONT_FILE;
        System.out.println(font);
    }
}