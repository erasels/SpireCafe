package spireCafe.interactables.patrons.trashking.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.trashking.TrashKingPatron;

import static spireCafe.Anniv7Mod.makeID;

public class PaintBucket extends AbstractSCRelic {
    public static final String ID = makeID(PaintBucket.class.getSimpleName());

    public PaintBucket() {
        super(ID, TrashKingPatron.assetID, AbstractRelic.RelicTier.SPECIAL, AbstractRelic.LandingSound.FLAT);
    }

    @Override
    public String getUpdatedDescription() {
        return DESCRIPTIONS[0];
    }

    @Override
    public AbstractRelic makeCopy() {
        return new PaintBucket();
    }
}
