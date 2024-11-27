package spireCafe.interactables.npcs.marisa;

import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.EventStrings;
import spireCafe.abstracts.AbstractCutscene;
import spireCafe.abstracts.AbstractNPC;

import static spireCafe.Anniv7Mod.makeID;

public class MarisaCutscene extends AbstractCutscene {
    public static final String ID = makeID(MarisaCutscene.class.getSimpleName());
    private static final EventStrings eventStrings = CardCrawlGame.languagePack.getEventString(ID);

    public MarisaCutscene(AbstractNPC character) {
        super(character, eventStrings);
    }

}