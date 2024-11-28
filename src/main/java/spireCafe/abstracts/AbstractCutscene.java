package spireCafe.abstracts;

import spireCafe.Anniv7Mod;
import spireCafe.ui.Dialog;
import spireCafe.util.TexLoader;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.vfx.AbstractGameEffect;

public abstract class AbstractCutscene extends AbstractGameEffect {
    public final String[] DESCRIPTIONS;
    public final String[] OPTIONS;
    private final TextureRegion TEXTBOX;
    protected final Dialog dialog = new Dialog(this);
    protected int dialogueIndex;
    protected final Hitbox hb;
    private boolean show;
    protected AbstractNPC character;
    protected boolean isNPCSpeaking = false;
    protected float blackScreenValue = 0.4f;
    public static boolean isInCutscene = false;

    public AbstractCutscene(AbstractNPC character, EventStrings eventStrings) {
        Texture TEXTBOX_TEXTURE = TexLoader.getTexture(Anniv7Mod.makeUIPath("Textbox.png"));
        TEXTBOX = new TextureRegion(TEXTBOX_TEXTURE);
        DESCRIPTIONS = eventStrings.DESCRIPTIONS;
        OPTIONS = eventStrings.OPTIONS;
        this.character = character;
        this.hb = new Hitbox(Settings.WIDTH, Settings.HEIGHT);
        this.hb.x = 0.0F;
        this.hb.y = 0.0F;
        this.show = true;
        if(character.alreadyPerformedInteraction){
            this.dialogueIndex=DESCRIPTIONS.length-1;
        } else {
            this.dialogueIndex = 0;
        }
    }

    public void update() {
        if (!shouldRunCutscene()) {
            return;
        }
        if (this.show) {
            AbstractDungeon.overlayMenu.showBlackScreen(blackScreenValue);
            isInCutscene = true;
            updateDialogueText();
        }
        this.hb.update();
        if (Dialog.optionList.isEmpty()) {
            if (this.hb.hovered && InputHelper.justClickedLeft) {
                InputHelper.justClickedLeft = false;
                this.hb.clickStarted = true;
            }
            if (this.hb.clicked) {
                this.hb.clicked = false;
                onClick();
            }
        }
        this.dialog.update();
    }

    protected void onClick() {
        nextDialogue();
    }

    protected void nextDialogue() {
        this.dialogueIndex++;
        goToDialogue(dialogueIndex);
    }

    protected void goToDialogue(int newIndex) {
        Dialog.optionList.clear();
        this.dialogueIndex = newIndex;
        if (this.dialogueIndex < DESCRIPTIONS.length) {
            updateDialogueText();
        } else {
            endCutscene();
        }
    }

    protected void endCutscene() {
        AbstractDungeon.overlayMenu.hideBlackScreen();
        isInCutscene = false;
        this.isDone = true;
    }

    protected void backToCutscene() {
        AbstractDungeon.overlayMenu.showBlackScreen(blackScreenValue);
    }

    public void render(SpriteBatch sb) {
        if (!shouldRunCutscene()) {
            return;
        }
        sb.setColor(Color.WHITE.cpy());
        character.renderCutscenePortrait(sb);
        sb.draw(TEXTBOX, Dialog.DIALOG_MSG_X - (100.0f * Settings.scale), (-170.0f * Settings.scale), 0.0f, 0.0f, TEXTBOX.getRegionWidth(), TEXTBOX.getRegionHeight(), Settings.scale, Settings.scale, 0.0f);
        this.dialog.render(sb);
    }

    public boolean shouldRunCutscene() {
        return !AbstractDungeon.isScreenUp;
    }

    protected void updateDialogueText() {
        String text = appendSpeakerToDialogue(DESCRIPTIONS[this.dialogueIndex]);
        if (this.show) {
            this.show = false;
            this.dialog.show(text);
        } else {
            this.dialog.updateBodyText(text);
        }
    }

    protected String appendSpeakerToDialogue(String dialogue) {
        String speakerName = FontHelper.colorString(character.name, "y") + " NL ";
        if (dialogue.contains("\"") || isNPCSpeaking) {
            return speakerName + dialogue;
        } else {
            return dialogue;
        }
    }

    public void dispose() {
        this.dialog.clear();
    }

}