package spireCafe.interactables.patrons.powerelic;

import com.megacrit.cardcrawl.cards.AbstractCard;
import spireCafe.util.Wiz;

import java.util.ArrayList;

public class PowerelicConfig {
    public static int calculateNumberOfRelicsToConvert(ArrayList<AbstractCard> selectedCards){
        int total=0;
        for(AbstractCard card : selectedCards) {
            int effectivePowerCost = card.cost;
            if (effectivePowerCost < 0) effectivePowerCost = Wiz.adp().energy.energyMaster;
            total += (effectivePowerCost + 1);
        }
        return total;
    }
}
