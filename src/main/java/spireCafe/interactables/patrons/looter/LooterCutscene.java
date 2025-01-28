package spireCafe.interactables.patrons.looter;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.GainPennyEffect;
import spireCafe.CafeRoom;
import spireCafe.abstracts.AbstractCafeInteractable;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;
import spireCafe.abstracts.AbstractPatron;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

import java.util.ArrayList;
import java.util.List;

import static spireCafe.Anniv7Mod.makeID;

public class LooterCutscene extends AbstractCutscene {
    public static final String ID = makeID(LooterCutscene.class.getSimpleName());
    private static final CutsceneStrings cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(ID);

    int rewardGold;
    public LooterCutscene(AbstractNPC character) {
        super(character, cutsceneStrings);

        LooterPatron looter = ((LooterPatron)character);
        rewardGold = looter.rewardGold;
        if(looter.stealTarget == null){ //First interaction with looter: generate steal target
            ArrayList<AbstractPatron> possibleTargets = new ArrayList<>();
            if(AbstractDungeon.getCurrRoom().event instanceof CafeRoom){ //sanity check idk
                CafeRoom cafe = (CafeRoom)AbstractDungeon.getCurrRoom().event;
                List<AbstractCafeInteractable> inhabitants = cafe.getCurrentInhabitants();
                for(AbstractCafeInteractable i : inhabitants){
                    if(i instanceof AbstractPatron && i != character){
                        possibleTargets.add((AbstractPatron)i);
                    }
                }
            }
            looter.stealTarget = possibleTargets.get(AbstractDungeon.miscRng.random(0, possibleTargets.size()-1));

            if(looter.stealTarget.alreadyPerformedTransaction){ //(not briefed path)
                dialogueIndex = 7;
            }
        }else if(looter.stealTarget.alreadyPerformedTransaction){ //briefed, target interacted
            dialogueIndex = 4;
        }else{ //target not interacted
            dialogueIndex = 3;
        }
    }

    @Override
    protected void onClick() {
        switch (dialogueIndex){
            case 0: //looter beckons
                this.dialogueIndex = 1;
                this.dialog.updateBodyText(appendSpeakerToDialogue(String.format(DESCRIPTIONS[1], ((LooterPatron)character).stealTarget.name)));
                break;
            case 1: //flavor explanation (briefed)
            case 3: //looter waits
                this.dialogueIndex = 2;
                this.dialog.updateBodyText(appendSpeakerToDialogue(String.format(DESCRIPTIONS[2], ((LooterPatron)character).stealTarget.name, LooterPatron.MIN_GOLD, LooterPatron.MAX_GOLD)));
                break;
            case 2: //clearer explanation
                endCutscene();
                break;
            case 4: //walk to looter to receive reward
                if(rewardGold <= 10){ //different low gold output
                    this.dialogueIndex = 6;
                    this.dialog.updateBodyText(appendSpeakerToDialogue(String.format(DESCRIPTIONS[6], rewardGold)));
                }else{
                    this.dialogueIndex = 5;
                    this.dialog.updateBodyText(appendSpeakerToDialogue(String.format(DESCRIPTIONS[5], rewardGold)));
                }
                break;
            case 5: //regular reward: briefed
            case 8: //not briefed
                AbstractDungeon.player.gainGold(rewardGold);
                for(int i = 0; i < rewardGold; ++i) {
                    AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player,character.animationX, character.animationY, Settings.WIDTH*0.1f, Settings.HEIGHT*0.1f, false));
                }
                character.alreadyPerformedTransaction = true;
                character.blockingDialogueIndex = 0;
                endCutscene();
                break;
            case 6: //low gold output: briefed
                AbstractDungeon.player.gainGold(rewardGold);
                for(int i = 0; i < rewardGold; ++i) {
                    AbstractDungeon.effectList.add(new GainPennyEffect(AbstractDungeon.player,character.animationX, character.animationY, Settings.WIDTH*0.1f, Settings.HEIGHT*0.1f, false));
                }
                character.alreadyPerformedTransaction = true;
                character.blockingDialogueIndex = 1;
                endCutscene();
                break;
            case 7: //talking to looter for the first time after already stealing from target (not briefed)
                this.dialogueIndex = 8;
                this.dialog.updateBodyText(appendSpeakerToDialogue(String.format(DESCRIPTIONS[8], rewardGold)));
                break;
            default:
                nextDialogue();
        }
    }
}