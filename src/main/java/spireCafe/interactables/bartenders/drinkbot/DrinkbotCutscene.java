package spireCafe.interactables.bartenders.drinkbot;

import com.badlogic.gdx.math.MathUtils;
import spireCafe.abstracts.BartenderCutscene;
import spireCafe.util.cutsceneStrings.CutsceneStrings;

public class DrinkbotCutscene extends BartenderCutscene {

    private DrinkbotBartender drinkbot; // I'm not casting all that.
    private int jokesTold;

    public DrinkbotCutscene(DrinkbotBartender character, CutsceneStrings cutsceneStrings) {
        super(character, cutsceneStrings);
        this.drinkbot = character;
        this.drinkbot.hasPurchased = false;
        this.dialogueIndex = this.drinkbot.hasVisited ? 5 : 0;
        this.jokesTold = 0;
    }

    @Override
    protected void onClick() {
        switch (this.dialogueIndex) {
            case 2:
                nextDialogue();
                addAvailableOptions();
                break;
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
                goToDialogue(4);
                addAvailableOptions();
                break;
            case 10:
            case 11:
                endCutscene();
                break;
            default:
                nextDialogue();
                break;
        }

    }

    @Override
    protected void addSecondOption() {
        String secondOptionDesc = bartender.getSecondOptionDescription();
        this.dialog.addDialogOption(secondOptionDesc, !bartender.getSecondOptionCondition(), new DrinkInserterRelic()).setOptionResult((i) -> {
            this.bartender.inSecondAction = true;
            this.bartender.applySecondOptionAction();
            this.bartender.secondUsed = true;
            handleAfterGameplayOptionChosen();
        });
    }

    @Override
    protected void addNoThanksOption() {
        String noThanks = bartender.getNoThanksDescription();
        this.dialog.addDialogOption(noThanks).setOptionResult((i)->{
            if (allGameplayOptionsDone()) {
                character.alreadyPerformedTransaction = true;
            }
            goToDialogue(getLastLineIndex());
        });
    }
    
    @Override
    protected void addFlavorOptions() {
        String flavor = OPTIONS[3];
        this.dialog.addDialogOption(flavor).setOptionResult((i)->{
            goToDialogue(this.jokesTold + 6);
            this.jokesTold++;
            if (this.jokesTold > 3 ) {
                this.jokesTold = 0;
            }
        });

    }
    
    @Override
    protected void handleAfterGameplayOptionChosen() {
        drinkbot.hasPurchased = true;
        goToDialogue(4);
        addAvailableOptions();
        if (allGameplayOptionsDone()) {
            character.alreadyPerformedTransaction = true;
        }
    }

    private int getLastLineIndex() {
        if (drinkbot.hasPurchased) {
            return DESCRIPTIONS.length - 1;
        } else {
            return DESCRIPTIONS.length - 2;
        }
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public String getBlockingDialogue() {
        int txt = MathUtils.random(0, BLOCKING_TEXTS.length - 1);
        String ret = BLOCKING_TEXTS[txt];
        if (txt == 1) {
            ret = String.format(ret, this.drinkbot.nemesisNum);
        }
        return ret;
    }
   
    
}