package spireCafe.interactables.merchants.snackmaster.food;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.vfx.combat.HealEffect;
import spireCafe.abstracts.AbstractMerchant;

import static spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant.ZINGER_CUTOFF;
import static spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant.snackmasterStrings;

public class LouseBurger extends AbstractFoodArticle{
    public LouseBurger(AbstractMerchant merchant, int slot) {
        super("LouseBurger", merchant, slot, "louseBurger");
    }

    @Override
    public void foodEffect() {
        AbstractDungeon.topLevelEffectsQueue.add(new HealEffect(xPos, yPos, 10));
    }

    @Override
    public int getBasePrice() {
        return 75;
    }

    @Override
    public String getTipHeader() {
        return snackmasterStrings.TEXT[ZINGER_CUTOFF+1];
    }

    @Override
    public String getTipBody() {
        return snackmasterStrings.TEXT[ZINGER_CUTOFF+2];
    }
}
