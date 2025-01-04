package spireCafe.interactables.bartenders.drinkbot;

import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.actions.defect.ChannelAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCRelic;

public class DrinkInserterRelic extends AbstractSCRelic{

    private static final String ID = Anniv7Mod.makeID(DrinkInserterRelic.class.getSimpleName());

    public DrinkInserterRelic() {
        super(ID, RelicTier.SPECIAL, LandingSound.CLINK);
    }

    @Override
    public void onEquip() {
        this.counter = 0;
    }

    @Override
    public void atTurnStart() {
        if (this.counter == -1) {
            this.counter = 3;
        } else {
            this.counter++;
        }

        if (this.counter == 3) {
            this.counter = 0;
            flash();
            addToBot(new RelicAboveCreatureAction(AbstractDungeon.player, this));
            addToBot(new ChannelAction(new BloodAlcoholOrb()));
        }
    }
    
}
