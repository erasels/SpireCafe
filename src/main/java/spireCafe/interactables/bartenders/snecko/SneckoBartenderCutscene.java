package spireCafe.interactables.bartenders.snecko;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractBartender;
import spireCafe.abstracts.BartenderCutscene;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class SneckoBartenderCutscene extends BartenderCutscene {
    private CutsceneStrings cutsceneStrings;
    public SneckoBartenderCutscene(AbstractBartender character) {
        super(character);
        cutsceneStrings = LocalizedCutsceneStrings.getCutsceneStrings(Anniv7Mod.makeID(bartender.id + "Cutscene"));
    }

    @Override
    public String getBlockingDialogue() {
        int blockingDialogueIndex = character.blockingDialogueIndex;
        String blockingDialogue = cutsceneStrings.BLOCKING_TEXTS[blockingDialogueIndex];
        if(blockingDialogueIndex<cutsceneStrings.BLOCKING_TEXTS.length-1){
            character.blockingDialogueIndex++;
        }
        return blockingDialogue;
    }

    @Override
    protected void handleAfterGameplayOptionChosen() {
        SneckoBartender bartender = (SneckoBartender) this.bartender;
        // If healUsed is true and secondUsed is not, then the heal action was just taken
        // If healUsed is true and firstInteractionWasHeal is false, then the heal action was just taken (and the second action has been taken earlier)
        if(bartender.healUsed && (!bartender.secondUsed || !bartender.firstInteractionWasHeal)){
            goToDialogue(2);
            bartender.firstInteractionWasHeal = true;
        } else if(bartender.secondUsed && (!bartender.healUsed || bartender.firstInteractionWasHeal)){
            goToDialogue(3);
            bartender.firstInteractionWasHeal = false;
        }
        // If there are still unselected gameplay options, we show them again along with flavor and no thanks.
        if (!allGameplayOptionsDone()) {
            addAvailableOptions();
        } else {
            // All gameplay done, block further dialogue, disregard flavor.
            character.alreadyPerformedTransaction = true;
        }
    }

    @Override
    protected void onClick() {
        if (dialogueIndex == 0) {
            nextDialogue();
            addAvailableOptions();
        } else {
            // If we have reached beyond the last description, or chosen "No thanks" at the end, end the cutscene.
            if (dialogueIndex >= DESCRIPTIONS.length - 1) {
                endCutscene();
            } else {
                if(bartender.healUsed && bartender.secondUsed){
                    endCutscene();
                } else {
                    nextDialogue();
                }
            }
        }
    }
}
