package spireCafe.relics;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.cards.Clone;

import static spireCafe.Anniv7Mod.makeID;
import static spireCafe.util.Wiz.*;

public class BookOfRCorp extends AbstractSCRelic {

    public static final String ID = makeID(BookOfRCorp.class.getSimpleName());

    public BookOfRCorp() {
        super(ID, RelicTier.SPECIAL, LandingSound.HEAVY);
    }

    @Override
    public void atBattleStartPreDraw() {
        this.flash();
        atb(new RelicAboveCreatureAction(adp(), this));
        makeInHand(new Clone());
    }
}
