package spireCafe.interactables.patrons.powerelic.implementation.patches;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import spireCafe.util.Wiz;

public class TipHelperRelicDetection {
    public static boolean detectRelics(Texture img){
        for(AbstractRelic relic : Wiz.adp().relics){
            if(img==relic.img){
                return true;
            }
        }
        return false;
    }
}
