package spireCafe.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.DialogWord;
import com.megacrit.cardcrawl.ui.DialogWord.AppearEffect;
import com.megacrit.cardcrawl.ui.DialogWord.WordColor;
import com.megacrit.cardcrawl.ui.DialogWord.WordEffect;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.interactables.patrons.missingno.MissingnoCutscene;
import spireCafe.interactables.patrons.missingno.MissingnoPatron;

import java.util.ArrayList;
import java.util.Scanner;

public class Dialog {
    private Color color = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    private Color targetColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
    private static final Color PANEL_COLOR = new Color(0.0F, 0.0F, 0.0F, 0.5F);
    private boolean isMoving = false;
    private float animateTimer = 0.0F;
    private boolean show = false;
    private float curLineWidth = 0.0F;
    private int curLine = 0;
    private AppearEffect a_effect;
    private Scanner s;
    private final GlyphLayout gl = new GlyphLayout();
    private static BitmapFont font;
    private final ArrayList<DialogWord> words = new ArrayList<>();
    private boolean textDone = true;
    private float wordTimer = 0.0F;
    private static final float CHAR_SPACING;
    private static final float LINE_SPACING;
    public static final float DIALOG_MSG_X;
    public static final float DIALOG_MSG_Y;
    public static final float DIALOG_MSG_W;
    public static ArrayList<OptionButton> optionList;
    public AbstractCutscene cutscene;

    public Dialog(AbstractCutscene cutscene) {
        this.cutscene = cutscene;
    }

    public void update() {
        if (this.isMoving) {
            this.animateTimer -= Gdx.graphics.getDeltaTime();
            if (this.animateTimer < 0.0F) {
                this.animateTimer = 0.0F;
                this.isMoving = false;
            }
        }

        this.color = this.color.lerp(this.targetColor, Gdx.graphics.getDeltaTime() * 8.0F);
        if (this.show) {
            for(int i = 0; i < optionList.size(); i++) {
                (optionList.get(i)).update(optionList.size());
            }
        }

        if(cutscene.getCharacter() != null && cutscene.getCharacter().id.equals(MissingnoPatron.ID)){
            font = MissingnoCutscene.font;
        } else {
            font = FontHelper.charDescFont;
        }

        if (Settings.lineBreakViaCharacter) {
            this.bodyTextEffectCN();
        } else {
            this.bodyTextEffect();
        }

        for (DialogWord w : this.words) {
            w.update();
        }
    }

    public void clear() {
        optionList.clear();
        this.words.clear();
    }

    public void show() {
        this.targetColor = PANEL_COLOR;
        if (Settings.FAST_MODE) {
            this.animateTimer = 0.125F;
        } else {
            this.animateTimer = 0.5F;
        }
        this.show = true;
        this.isMoving = true;
    }

    public void show(String text) {
        this.updateBodyText(text);
        this.show();
    }

    public void hide() {
        this.targetColor = new Color(0.0F, 0.0F, 0.0F, 0.0F);
        if (Settings.FAST_MODE) {
            this.animateTimer = 0.125F;
        } else {
            this.animateTimer = 0.5F;
        }

        this.show = false;
        this.isMoving = true;

        for (DialogWord w : this.words) {
            w.dialogFadeOut();
        }
        optionList.clear();
    }

    public void removeDialogOption(int slot) {
        optionList.remove(slot);
    }

    public OptionButton addDialogOption(String text) {
        OptionButton button = new OptionButton(this, optionList.size(), text);
        optionList.add(button);
        return button;
    }

    public OptionButton addDialogOption(String text, AbstractCard previewCard) {
        OptionButton button = new OptionButton(this, optionList.size(), text, previewCard);
        optionList.add(button);
        return button;
    }

    public OptionButton addDialogOption(String text, AbstractRelic previewRelic) {
        OptionButton button = new OptionButton(this, optionList.size(), text, previewRelic);
        optionList.add(button);
        return button;
    }

    public OptionButton addDialogOption(String text, AbstractCard previewCard, AbstractRelic previewRelic) {
        OptionButton button = new OptionButton(this, optionList.size(), text, previewCard, previewRelic);
        optionList.add(button);
        return button;
    }

    public OptionButton addDialogOption(String text, boolean isDisabled) {
        OptionButton button = new OptionButton(this, optionList.size(), text, isDisabled);
        optionList.add(button);
        return button;
    }

    public OptionButton addDialogOption(String text, boolean isDisabled, AbstractCard previewCard) {
        OptionButton button = new OptionButton(this, optionList.size(), text, isDisabled, previewCard);
        optionList.add(button);
        return button;
    }

    public OptionButton addDialogOption(String text, boolean isDisabled, AbstractRelic previewRelic) {
        OptionButton button = new OptionButton(this, optionList.size(), text, isDisabled, previewRelic);
        optionList.add(button);
        return button;
    }

    public OptionButton addDialogOption(String text, boolean isDisabled, AbstractCard previewCard, AbstractRelic previewRelic) {
        OptionButton button = new OptionButton(this, optionList.size(), text, isDisabled, previewCard, previewRelic);
        optionList.add(button);
        return button;
    }

    public void updateBodyText(String text) {
        this.updateBodyText(text, AppearEffect.BUMP_IN);
    }

    public void updateBodyText(String text, AppearEffect ae) {
        this.s = new Scanner(text);

        this.words.clear();
        this.textDone = false;
        this.a_effect = ae;
        this.curLineWidth = 0.0F;
        this.curLine = 0;
    }

    private void bodyTextEffectCN() {
        this.wordTimer -= Gdx.graphics.getDeltaTime();
        if (this.wordTimer < 0.0F && !this.textDone) {
            if (Settings.FAST_MODE) {
                this.wordTimer = 0.005F;
            } else {
                this.wordTimer = 0.02F;
            }

            if (this.s.hasNext()) {
                String word = this.s.next();
                if (word.equals("NL")) {
                    this.curLine++;
                    this.curLineWidth = 0.0F;
                    return;
                }

                WordColor color = DialogWord.identifyWordColor(word);
                if (color != WordColor.DEFAULT) {
                    word = word.substring(2);
                }

                WordEffect effect = DialogWord.identifyWordEffect(word);
                if (effect != WordEffect.NONE) {
                    word = word.substring(1, word.length() - 1);
                }

                for(int i = 0; i < word.length(); i++) {
                    String tmp = Character.toString(word.charAt(i));
                    this.gl.setText(font, tmp);
                    if (this.curLineWidth + this.gl.width > DIALOG_MSG_W) {
                        this.curLine++;
                        this.curLineWidth = this.gl.width;
                    } else {
                        this.curLineWidth += this.gl.width;
                    }

                    this.words.add(new DialogWord(font, tmp, this.a_effect, effect, color, DIALOG_MSG_X + this.curLineWidth - this.gl.width, DIALOG_MSG_Y - LINE_SPACING * (float)this.curLine, this.curLine));
                    if (!this.show) {
                        this.words.get(this.words.size() - 1).dialogFadeOut();
                    }
                }
            } else {
                this.textDone = true;
                this.s.close();
            }
        }
    }

    private void bodyTextEffect() {
        this.wordTimer -= Gdx.graphics.getDeltaTime();
        if (this.wordTimer < 0.0F && !this.textDone) {
            if (Settings.FAST_MODE) {
                this.wordTimer = 0.005F;
            } else {
                this.wordTimer = 0.02F;
            }

            if (this.s.hasNext()) {
                String word = this.s.next();
                if (word.equals("NL")) {
                    this.curLine++;
                    this.curLineWidth = 0.0F;
                    return;
                }

                WordColor color = DialogWord.identifyWordColor(word);
                if (color != WordColor.DEFAULT) {
                    word = word.substring(2);
                }

                WordEffect effect = DialogWord.identifyWordEffect(word);
                if (effect != WordEffect.NONE) {
                    word = word.substring(1, word.length() - 1);
                }

                this.gl.setText(font, word);
                if (this.curLineWidth + this.gl.width > DIALOG_MSG_W) {
                    this.curLine++;
                    this.curLineWidth = this.gl.width + CHAR_SPACING;
                } else {
                    this.curLineWidth += this.gl.width + CHAR_SPACING;
                }

                this.words.add(new DialogWord(font, word, this.a_effect, effect, color, DIALOG_MSG_X + this.curLineWidth - this.gl.width, DIALOG_MSG_Y - LINE_SPACING * (float)this.curLine, this.curLine));
                if (!this.show) {
                    this.words.get(this.words.size() - 1).dialogFadeOut();
                }
            } else {
                this.textDone = true;
                this.s.close();
            }
        }
    }

    public void render(SpriteBatch sb) {
        if (!AbstractDungeon.player.isDead) {
            for (DialogWord word : this.words) {
                word.render(sb, -50.0F * Settings.scale);
            }
            for (OptionButton button : optionList) {
                button.render(sb);
            }
            for (OptionButton button : optionList) {
                button.renderCardPreview(sb);
                button.renderRelicPreview(sb);
            }
        }
    }

    static {
        CHAR_SPACING = 8.0F * Settings.scale;
        LINE_SPACING = 34.0F * Settings.scale;
        DIALOG_MSG_X = (float)Settings.WIDTH * 0.23F;
        DIALOG_MSG_Y = 250.0F * Settings.scale;
        DIALOG_MSG_W = (float)Settings.WIDTH * 0.6F;
        optionList = new ArrayList<>();
    }
}
