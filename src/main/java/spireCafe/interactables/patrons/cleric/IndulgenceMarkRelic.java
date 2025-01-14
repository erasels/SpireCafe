package spireCafe.interactables.patrons.cleric;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.powers.DexterityPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractSCRelic;
import spireCafe.util.Wiz;

public class IndulgenceMarkRelic extends AbstractSCRelic {

    private static final String ID = Anniv7Mod.makeID(IndulgenceMarkRelic.class.getSimpleName());

    public IndulgenceMarkRelic() {
        super(ID, "ClericPatron", RelicTier.SPECIAL, LandingSound.MAGICAL);
        this.counter = 0;
    }
    
    @Override
    public void onPlayerEndTurn() {
        super.onPlayerEndTurn();
        if (EnergyPanel.totalCount > 0 && this.counter < 3) {
            flash();
            addToBot(new ApplyPowerAction(Wiz.p(), Wiz.p(), new StrengthPower(Wiz.p(), 1)));
            addToBot(new ApplyPowerAction(Wiz.p(), Wiz.p(), new DexterityPower(Wiz.p(), 1)));
            this.counter++;
        }
    }

    @Override
    public void onEnterRoom(AbstractRoom room) {
        super.onEnterRoom(room);
        this.counter = 0;
    }
    
}
