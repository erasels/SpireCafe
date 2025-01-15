package spireCafe.interactables.patrons.powerelic;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import spireCafe.util.Wiz;

public class ActivatePowerelicAction extends AbstractGameAction {

    private PowerelicCard card;
    public ActivatePowerelicAction(PowerelicCard card){
        this.card=card;
    }
    @Override
    public void update() {
        if(card.capturedRelic == null){
            //if we're here, we somehow have a powerelic card without a relic inside.
            //this is probably due to some save file glitch we overlooked.  or a Prismatic effect of some sort.
            isDone=true;
            return;
        }

        card.activateRelicFromHand(card.capturedRelic);
        //note that if the card was duplicated, capturedRelic might point to a different relic now!
        if(PowerelicAllowlist.isEssentialEquipRelic(card.capturedRelic)) {
            int previousMaxEnergy=Wiz.adp().energy.energyMaster;
            int previousHandSize=Wiz.adp().masterHandSize;
            card.capturedRelic.onEquip();
            if(PowerelicAllowlist.isImmediateOnequipRelic(card.capturedRelic)){
                Wiz.att(new PowerelicUpdateEnergyAndHandsizeAction(previousMaxEnergy,previousHandSize));
            }
        }

        isDone=true;
    }
}
