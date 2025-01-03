package spireCafe.interactables.bartenders.drinkbot;

import com.badlogic.gdx.math.MathUtils;

import spireCafe.abstracts.BartenderCutscene;
import spireCafe.util.cutsceneStrings.CutsceneStrings;

public class DrinkbotCutscene extends BartenderCutscene {

    private DrinkbotBartender drinkbot; // I'm not casting all that.

    public DrinkbotCutscene(DrinkbotBartender character, CutsceneStrings cutsceneStrings) {
        super(character, cutsceneStrings);
        this.drinkbot = character;
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
