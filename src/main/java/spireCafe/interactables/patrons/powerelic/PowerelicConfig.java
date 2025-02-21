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
    /*
        The following constants are for use with the Violescent Shard relic.
        Relic substitution chance is subject to the following:
            Chance is rolled once per 3-card reward
            If roll is successful, the card corresponding to (floornum % numCards) is swapped out
            Any cardable relic can be chosen from the first four relic pools
            If the original card's rarity was RARE, a boss relic will be chosen instead
            Relic chosen is dependent on cardRng. If a non-cardable relic is chosen, it is rerolled
            Only the final chosen relic is removed from the relic pool
    */
    public static final float CARDED_RELIC_SUBSTITUTION_CHANCE = 0.70f;
    public static final int RELIC_COST = 100;
    public static final boolean EXCLUDE_BOSS_ROOMS = true;
    public static final boolean DEBUG_ALL_RELICS_ARE_BOSS_RELICS = false;

}
