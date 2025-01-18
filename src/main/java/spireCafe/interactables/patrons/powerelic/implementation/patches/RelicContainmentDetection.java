package spireCafe.interactables.patrons.powerelic.implementation.patches;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.interactables.patrons.powerelic.implementation.PowerelicCard;

public class RelicContainmentDetection {
    public static boolean isContained(AbstractRelic relic){
        return PowerelicCard.PowerelicRelicContainmentFields.isContained.get(relic);
    }
}
