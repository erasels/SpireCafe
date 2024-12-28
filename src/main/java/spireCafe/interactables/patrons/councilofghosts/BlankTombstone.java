package spireCafe.interactables.patrons.councilofghosts;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.IntangiblePlayerPower;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.interactables.patrons.purpletear.BookOfIndex;

import static spireCafe.Anniv7Mod.makeID;

public class BlankTombstone extends AbstractSCRelic {
    public static final String ID = makeID(BlankTombstone.class.getSimpleName());
    private static final int TURNS = 1;

    public BlankTombstone() {
        super(ID, CouncilOfGhostsPatron.assetID, RelicTier.SPECIAL, LandingSound.SOLID);
    }

    @Override
    public void atBattleStart() {
        flash();
        addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
        addToBot(new ApplyPowerAction(AbstractDungeon.player, (AbstractCreature)null, new IntangiblePlayerPower(AbstractDungeon.player, TURNS), TURNS));
    }
}
