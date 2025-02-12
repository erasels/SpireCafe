package spireCafe.interactables.bartenders.Temmie;

import spireCafe.abstracts.BartenderCutscene;
import spireCafe.util.cutsceneStrings.CutsceneStrings;

public class TemmieBartenderCutscene extends BartenderCutscene {
    public TemmieBartenderCutscene(TemmieBartender character, CutsceneStrings cutsceneStrings) {
        super(character, cutsceneStrings);
    }

    @Override
    protected void addSecondOption() {
        String secondOptionDesc = bartender.getSecondOptionDescription();

        // Hide the donation option if Temmie has gone to college
        if (secondOptionDesc.isEmpty()) {
            return;
        }

        this.dialog.addDialogOption(secondOptionDesc, !bartender.getSecondOptionCondition()).setOptionResult((i) -> {
            bartender.inSecondAction = true;
            bartender.applySecondOptionAction();

            // Keep donation available until the goal is reached
            if (!((TemmieBartender) bartender).hasReceivedArmor) {
                bartender.secondUsed = ((TemmieBartender) bartender).totalDonations >= 500;
            } else {
                bartender.secondUsed = true; // Hides donation button after armor is awarded
            }


            handleAfterGameplayOptionChosen();
        });
    }
}