package spireCafe.abstracts;

import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class BartenderCutscene extends AbstractCutscene {
    protected AbstractBartender bartender;

    public BartenderCutscene(AbstractBartender character) {
        super(character, getCutsceneStrings(character));
        this.bartender = character;
    }

    /*
     * You can access the contents via the public variables that are set in the super class (BLOCKING_TEXTS, OPTIONS, DESCRIPTIONS)
     * The cutscenes string are gotten through the id of the Bartender which is the name of the Bartender's class.
     */
    protected static CutsceneStrings getCutsceneStrings(AbstractBartender character) {
        return LocalizedCutsceneStrings.getCutsceneStrings(character.id);
    }

    @Override
    public String getBlockingDialogue() {
        // If already performed transaction, just return a blocking line.
        return BLOCKING_TEXTS[bartender.blockingDialogueIndex];
    }

    @Override
    protected void onClick() {
        // Default behavior: advance dialogue, show options only on the first interaction if not performed transaction.
        if (dialogueIndex == 0 && !alreadyPerformedTransaction) {
            // The next dialogue index (1) will show the player's options
            nextDialogue();
            addHealOption();
            addSecondOptionIfExists();
            addFlavorOptions();
            addNoThanksOption();
        } else {
            // If we have reached beyond the last description, or the player already transacted, end.
            if (dialogueIndex >= DESCRIPTIONS.length - 1) {
                endCutscene();
            } else {
                // By default, just advance the dialogue.
                nextDialogue();
            }
        }
    }

    private void addHealOption() {
        // Heal option. The bartender defines their own cost and heal amount.
        String healText = bartender.getHealOptionDescription();
        this.dialog.addDialogOption(healText).setOptionResult((i) -> {
            bartender.applyHealAction();
            character.alreadyPerformedTransaction = true;
            // Move to the next line of dialogue after heal
            nextDialogue();
        });
    }

    private void addSecondOptionIfExists() {
        String secondOptionDesc = bartender.getSecondOptionDescription();
        if (secondOptionDesc != null && !secondOptionDesc.isEmpty()) {
            this.dialog.addDialogOption(secondOptionDesc).setOptionResult((i) -> {
                bartender.applySecondOptionAction();
                character.alreadyPerformedTransaction = true;
                // Move to another part of the dialogue (assuming next line of descriptions)
                nextDialogue();
            });
        }
    }

    private void addNoThanksOption() {
        // A "No Thanks" or "Leave" option
        String noThanks = OPTIONS[1]; // Suppose OPTIONS[1] = "No thanks."
        this.dialog.addDialogOption(noThanks).setOptionResult((i)->{
            // Just move to a "goodbye" line or end directly
            goToDialogue(DESCRIPTIONS.length - 1); // Last description as goodbye
        });
    }

    /*
     * If you want custom flavor options where you can speak to the bartender, override this method.
     */
    protected void addFlavorOptions() {

    }
}
