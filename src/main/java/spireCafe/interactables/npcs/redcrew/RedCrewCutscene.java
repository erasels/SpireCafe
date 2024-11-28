package spireCafe.interactables.npcs.redcrew;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;

import static spireCafe.Anniv7Mod.makeID;

public class RedCrewCutscene extends AbstractCutscene {
    public static final String ID = makeID(RedCrewCutscene.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    public RedCrewCutscene(AbstractNPC character) {
        super(character, eventStrings);
    }

}