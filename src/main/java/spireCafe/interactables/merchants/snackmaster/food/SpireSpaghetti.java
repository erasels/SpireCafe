package spireCafe.interactables.merchants.snackmaster.food;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.snackmaster.persistent.SpireSpaghettiBlight;

import static spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant.ZINGER_CUTOFF;
import static spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant.snackmasterStrings;

public class SpireSpaghetti extends AbstractFoodArticle{


    public SpireSpaghetti(AbstractMerchant merchant, int slot) {
        super("SpireSpaghetti", merchant, slot, "spireSpaghetti");
    }

    @Override
    public void foodEffect() {
        AbstractDungeon.getCurrRoom().spawnBlightAndObtain(xPos, yPos, new SpireSpaghettiBlight());
    }

    @Override
    public int getBasePrice() {
        return 120;
    }

    @Override
    public String getTipHeader() {
        return snackmasterStrings.TEXT[ZINGER_CUTOFF+5];
    }

    @Override
    public String getTipBody() {
        return snackmasterStrings.TEXT[ZINGER_CUTOFF+6];
    }
}
