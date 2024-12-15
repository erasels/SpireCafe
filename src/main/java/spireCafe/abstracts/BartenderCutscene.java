package spireCafe.abstracts;

import spireCafe.Anniv7Mod;
import spireCafe.util.cutsceneStrings.CutsceneStrings;
import spireCafe.util.cutsceneStrings.LocalizedCutsceneStrings;

public class BartenderCutscene extends AbstractCutscene {
    protected AbstractBartender bartender;

    public BartenderCutscene(AbstractBartender character) {
        super(character, getCutsceneStrings(character));
        this.bartender = character;
    }

    public BartenderCutscene(AbstractBartender character, CutsceneStrings cutsceneStrings) {
        super(character, cutsceneStrings);
        this.bartender = character;
    }

    /*
     * Used if you don't define the cutscene strings yourself. If you make a subclass, just get them in a static variable and pass that to the overload.
     * You can access the contents via the public variables that are set in the super class (BLOCKING_TEXTS, OPTIONS, DESCRIPTIONS)
     * The cutscenes string are gotten through the id of the Bartender (with Cutscene appended) which is the name of the Bartender's class.
     * e.g. StarbucksBartender.class -> anniv7:VampireBartenderCutscene
     */
    protected static CutsceneStrings getCutsceneStrings(AbstractBartender character) {
        return LocalizedCutsceneStrings.getCutsceneStrings(Anniv7Mod.makeID(character.id + "Cutscene"));
    }

    @Override
    public String getBlockingDialogue() {
        return BLOCKING_TEXTS[bartender.blockingDialogueIndex];
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
                nextDialogue();

                // addAvailableOptions();
            }
        }
    }

    /**
     * Adds all available options that haven't been used yet.
     * Flavor and No Thanks are always added (if flavor is implemented).
     */
    private void addAvailableOptions() {
        // Clear old options, just in case
        this.dialog.clear();

        if (!bartender.healUsed) {
            addHealOption();
        }
        if (!bartender.secondUsed && bartender.getSecondOptionDescription() != null && !bartender.getSecondOptionDescription().isEmpty()) {
            addSecondOption();
        }

        addFlavorOptions();
        addNoThanksOption();
    }

    private void addHealOption() {
        String healText = bartender.getHealOptionDescription();
        this.dialog.addDialogOption(healText).setOptionResult((i) -> {
            bartender.applyHealAction();
            bartender.healUsed = true;
            handleAfterGameplayOptionChosen();
        });
    }

    private void addSecondOption() {
        String secondOptionDesc = bartender.getSecondOptionDescription();
        this.dialog.addDialogOption(secondOptionDesc).setOptionResult((i) -> {
            bartender.applySecondOptionAction();
            bartender.secondUsed = true;
            handleAfterGameplayOptionChosen();
        });
    }

    private void addNoThanksOption() {
        String noThanks = OPTIONS[0];
        this.dialog.addDialogOption(noThanks).setOptionResult((i)->{
            // If all gameplay-affecting options are done, we consider the transaction complete.
            // Move to a "goodbye" line or end directly.
            if (allGameplayOptionsDone()) {
                character.alreadyPerformedTransaction = true;
            }
            goToDialogue(DESCRIPTIONS.length - 1); // Move to last line (goodbye)
        });
    }

    /**
     * Override in subclass if you want to add flavor options for talking (non gameplay affecting)
     */
    protected void addFlavorOptions() {
    }

    /**
     * Checks if all gameplay-affecting options have been used.
     */
    private boolean allGameplayOptionsDone() {
        boolean secondOptionExists = bartender.getSecondOptionDescription() != null && !bartender.getSecondOptionDescription().isEmpty();
        // If second option didn't exist, it's considered "done" by default.
        if (!secondOptionExists) {
            return bartender.healUsed;
        } else {
            return bartender.healUsed && bartender.secondUsed;
        }
    }

    /**
     * After choosing a gameplay option, we display the next dialogue line and update the option list.
     * If all gameplay options are done, we won't show them again.
     */
    private void handleAfterGameplayOptionChosen() {
        nextDialogue();
        // If there are still unselected gameplay options, we show them again along with flavor and no thanks.
        if (!allGameplayOptionsDone()) {
            addAvailableOptions();
        } else {
            // All gameplay done, block further dialogue, disregard flavor.
            character.alreadyPerformedTransaction = true;
        }
    }
}
