package spireCafe.interactables.patrons.looter;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import spireCafe.Anniv7Mod;
import spireCafe.CafeRoom;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.interactables.patrons.redlouse.RedLousePatron;
import spireCafe.ui.Dialog;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.ArrayList;
import java.util.List;

import static spireCafe.Anniv7Mod.logger;
import static spireCafe.Anniv7Mod.makeID;

public class LooterCutscene extends AbstractCutscene {
    public static final String ID = makeID(LooterCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    int rewardGold;
    public LooterCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);

        LooterPatron looter = ((LooterPatron)character);
        rewardGold = looter.rewardGold;
        if(looter.stealTarget == null){ //First interaction
            ArrayList<AbstractPatron> possibleTargets = new ArrayList<>();
            if(AbstractDungeon.getCurrRoom().event instanceof CafeRoom){
                CafeRoom cafe = (CafeRoom)AbstractDungeon.getCurrRoom().event;
                List<AbstractCafeInteractable> inhabitants = cafe.getCurrentInhabitants();
                for(AbstractCafeInteractable i : inhabitants){
                    if(i instanceof AbstractPatron && i != character){
                        possibleTargets.add((AbstractPatron)i);
                    }
                }
            }
            looter.stealTarget = possibleTargets.get(AbstractDungeon.eventRng.random(0, possibleTargets.size()-1));

            if(looter.stealTarget.alreadyPerformedTransaction){
                if(rewardGold == 0){
                    dialogueIndex = 9;
                }else{
                    dialogueIndex = 7;
                }
            }
        }else if(looter.stealTarget.alreadyPerformedTransaction){ //target interacted
            dialogueIndex = 4;
        }else{ //target not interacted
            dialogueIndex = 3;
        }
    }

    @Override
    protected void onClick() {
        switch (dialogueIndex){
            case 0: //flavor + mention steal target
                this.dialogueIndex = 1;
                this.dialog.updateBodyText(appendSpeakerToDialogue(String.format(DESCRIPTIONS[1], ((LooterPatron)character).stealTarget.name)));
                break;
            case 1:
            case 3: //clearer explanation
                this.dialogueIndex = 2;
                this.dialog.updateBodyText(appendSpeakerToDialogue(String.format(DESCRIPTIONS[2], ((LooterPatron)character).stealTarget.name)));
                break;
            case 2:
                endCutscene();
                break;
            case 4:
                if(rewardGold == 0){
                    goToDialogue(6);
                }else{
                    this.dialogueIndex = 5;
                    this.dialog.updateBodyText(appendSpeakerToDialogue(String.format(DESCRIPTIONS[5], rewardGold)));
                }
                break;
            case 5:
            case 8: //regular gold reward: talked before steal and not
                AbstractDungeon.player.gainGold(rewardGold);
                for(int i = 0; i < rewardGold; ++i) {
                    AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player,character.animationX, character.animationY, Settings.WIDTH*0.1f, Settings.HEIGHT*0.1f, false));
                }
                character.alreadyPerformedTransaction = true;
                character.blockingDialogueIndex = 0;
                endCutscene();
                break;
            case 6:
            case 9: //0 gold output: talked before steal and not
                character.alreadyPerformedTransaction = true;
                character.blockingDialogueIndex = 1;
                endCutscene();
                break;
            case 7:
                this.dialogueIndex = 8;
                this.dialog.updateBodyText(appendSpeakerToDialogue(String.format(DESCRIPTIONS[8], rewardGold)));
                break;
            default:
                nextDialogue();
        }
    }
}