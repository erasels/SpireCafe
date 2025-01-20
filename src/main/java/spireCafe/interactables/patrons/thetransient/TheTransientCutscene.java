package spireCafe.interactables.patrons.thetransient;

import basemod.helpers.CardModifierManager;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.BurningBlood;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.cardmods.TransientMod;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;
import spireCafe.vfx.SimpleTextureLerp;

import java.util.ArrayList;

import static spireCafe.Anniv7Mod.makeID;

public class TheTransientCutscene extends AbstractCutscene {
    public static final String ID = makeID(TheTransientCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);
    private static final int MAX_HP_LOSS = 3;

    public TheTransientCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void onClick() {
        switch (dialogueIndex){
            case 0:
                nextDialogue();

                this.dialog.addDialogOption(String.format(OPTIONS[0], MAX_HP_LOSS), new BurningBlood(){ //lmao get hijacked idiot relic (altering relic preview text to display transient definition)
                    @Override
                    protected void initializeTips() {
                        this.tips.clear();
                        this.tips.add(new PowerTip(OPTIONS[2], OPTIONS[3]));
                        super.initializeTips();
                    }
                }).setOptionResult((i)->{
                    goToDialogue(2);
                });
                this.dialog.addDialogOption(OPTIONS[1]).setOptionResult((i)-> goToDialogue(7));
                break;
            case 3:
                for(SimpleTextureLerp e: textureLerps){
                    e.earlyEnd();
                }
                nextDialogue();

                this.dialog.addDialogOption(OPTIONS[5]).setOptionResult((i)->{
                    AbstractDungeon.player.decreaseMaxHealth(MAX_HP_LOSS);

                    CardGroup group = new CardGroup(CardGroup.CardGroupType.UNSPECIFIED);
                    for (AbstractCard card : AbstractDungeon.player.masterDeck.group) {
                        if (card.cost != -2 && !CardModifierManager.hasModifier(card, TransientMod.ID)) {
                            group.addToBottom(card);
                        }
                    }
                    AbstractDungeon.gridSelectScreen.open(group, 1, OPTIONS[4], false);

                    character.alreadyPerformedTransaction = true;
                    if(chillTime < 5){
                        goToDialogue(8);
                        character.blockingDialogueIndex = 2;
                    }else if(fullyFaded){
                        goToDialogue(6);
                        character.blockingDialogueIndex = 1;
                    }else{
                        goToDialogue(5);
                        character.blockingDialogueIndex = 0;
                    }
                });
                break;
            case 5:
            case 6:
            case 7:
            case 8:
                ((TheTransientPatron)character).postCutsceneFade();
                endCutscene();
                break;
            default:
                nextDialogue();
                break;

        }
    }

    float chillTime = 0;
    int chillInd = -1;
    float speedMult = 1; //sequence speeds up over time by speedMult, lerp effects speeds up by half as much (lots of things on screen over time)
    boolean fullyFaded = false;
    public static final float CHILL_FADE_TIME = 60;
    ArrayList<SimpleTextureLerp> textureLerps = new ArrayList<>();
    //grid selection for applying transient mod
    public void update() {
        super.update();

        //chillmode
        if(dialogueIndex == 3){
            chillTime += Gdx.graphics.getDeltaTime() * speedMult;

            if(chillTime % CHILL_FADE_TIME <= 5){
                chillInd = -1;
            }else if(chillInd < 0 && chillTime % CHILL_FADE_TIME > 8){ //right to left transient
                chillInd = 0;
                SimpleTextureLerp e = new SimpleTextureLerp(character.cutscenePortrait.getTexture(), Settings.WIDTH*1.8f, Settings.HEIGHT*0.2f, 0, 1.6f, 0.8f, Settings.WIDTH*0.2f, Settings.HEIGHT*0.2f, 0, 1.8f, 0, 20f/((speedMult+1f)/2));
                AbstractDungeon.effectsQueue.add(e);
                textureLerps.add(e);
            } else if (chillInd < 1 && chillTime % CHILL_FADE_TIME > 18) { //spinny transient
                chillInd = 1;
                SimpleTextureLerp e = new SimpleTextureLerp(character.cutscenePortrait.getTexture(), Settings.WIDTH/2f, Settings.HEIGHT/2f, 0, 0, 1, Settings.WIDTH/2f, Settings.HEIGHT/2f, 480, 1.5f, 0, 25f/((speedMult+1f)/2));
                AbstractDungeon.effectsQueue.add(e);
                textureLerps.add(e);
            } else if (chillInd < 2 && chillTime % CHILL_FADE_TIME > 30) { //left to right player
                chillInd = 2;
                SimpleTextureLerp e = new SimpleTextureLerp(AbstractDungeon.player.shoulder2Img, 0, Settings.HEIGHT/2f, 0, 1, 1, Settings.WIDTH, Settings.HEIGHT/2f, 0, 1, 0, 20f/((speedMult+1f)/2));
                AbstractDungeon.topLevelEffectsQueue.add(e);
                textureLerps.add(e);
            } else if (chillInd < 3 && chillTime % CHILL_FADE_TIME > 36) { //drop down transient
                chillInd = 3;
                SimpleTextureLerp e = new SimpleTextureLerp(character.cutscenePortrait.getTexture(), Settings.WIDTH*0.75f, Settings.HEIGHT*1.8f, 0, 1.2f, 0.8f, Settings.WIDTH*0.75f, Settings.HEIGHT*0.2f, 0, 1.2f, 0, 25f/((speedMult+1f)/2));
                AbstractDungeon.topLevelEffectsQueue.add(e);
                textureLerps.add(e);
            } else if (chillInd < 4 && chillTime % CHILL_FADE_TIME > 42) { //spinning player
                chillInd = 4;
                SimpleTextureLerp e = new SimpleTextureLerp(AbstractDungeon.player.shoulder2Img, 0, Settings.HEIGHT/2f, 0, 2, 1, Settings.WIDTH/2f, Settings.HEIGHT/2f, -800, 0, 0, 30f/((speedMult+1f)/2));
                AbstractDungeon.topLevelEffectsQueue.add(e);
                textureLerps.add(e);
            } else if (chillInd < 5 && chillTime % CHILL_FADE_TIME > 50) { //both slide from sides of screen
                chillInd = 5;
                speedMult *= 1.2f; //oh my god is it getting faster
                SimpleTextureLerp e = new SimpleTextureLerp(character.cutscenePortrait.getTexture(), Settings.WIDTH*-0.5f, Settings.HEIGHT/2f, 0, 1, 1, Settings.WIDTH, Settings.HEIGHT/2f, 0, 0, 0, 20f/((speedMult+1f)/2));
                AbstractDungeon.topLevelEffectsQueue.add(e);
                textureLerps.add(e);
                e = new SimpleTextureLerp(AbstractDungeon.player.shoulder2Img, Settings.WIDTH*1.5f, Settings.HEIGHT/2f, 0, 1, 1, 0, Settings.HEIGHT/2f, 0, 0, 0, 20f/((speedMult+1f)/2));
                AbstractDungeon.topLevelEffectsQueue.add(e);
                textureLerps.add(e);
            }

            //fade cutscene transient over time of chillin
            if(character instanceof TheTransientPatron){
                ((TheTransientPatron) character).cutsceneAlpha = Math.max(0, (CHILL_FADE_TIME-chillTime)/CHILL_FADE_TIME);
            }
            //mark the point which the player has fully chilled for transient to fade
            if(chillTime > CHILL_FADE_TIME){
                fullyFaded = true;
            }
        }

        //simple grid select logic
        if (!AbstractDungeon.gridSelectScreen.selectedCards.isEmpty()) {
            for (int i = 0; i < AbstractDungeon.gridSelectScreen.selectedCards.size(); i++) {
                AbstractCard c = AbstractDungeon.gridSelectScreen.selectedCards.get(i);
                CardModifierManager.addModifier(c, new TransientMod());
            }
            AbstractDungeon.gridSelectScreen.selectedCards.clear();
            backToCutscene();
        }
    }

    @Override
    public void render(SpriteBatch sb) {
        super.render(sb);
    }
}