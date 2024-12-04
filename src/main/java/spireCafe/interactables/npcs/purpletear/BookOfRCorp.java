package spireCafe.interactables.npcs.purpletear;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import spireCafe.abstracts.AbstractSCRelic;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.interactables.npcs.purpletear.PurpleTearPatron.assetID;
import static spireCafe.util.Wiz.*;

public class BookOfRCorp extends AbstractSCRelic {

    public static final String ID = makeID(BookOfRCorp.class.getSimpleName());

    public BookOfRCorp() {
        super(ID, assetID,RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        atb(new RelicAboveCreatureAction(adp(), this));
        makeInHand(new Clone());
    }
}
