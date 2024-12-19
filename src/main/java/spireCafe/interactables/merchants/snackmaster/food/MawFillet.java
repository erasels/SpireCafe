package spireCafe.interactables.merchants.snackmaster.food;

import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.interactables.merchants.snackmaster.persistent.MawFilletBlight;

import static spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant.ZINGER_CUTOFF;
import static spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant.snackmasterStrings;

public class MawFillet extends AbstractFoodArticle{
    public MawFillet(AbstractMerchant merchant, int slot) {
        super("MawFillet", merchant, slot, "mawFillet");
    }

    @Override
    public void foodEffect() {
        AbstractDungeon.getCurrRoom().spawnBlightAndObtain(xPos, yPos, new MawFilletBlight());
    }

    @Override
    public int getBasePrice() {
        return 85;
    }

    @Override
    public String getTipHeader() {
        return snackmasterStrings.TEXT[ZINGER_CUTOFF+3];
    }

    @Override
    public String getTipBody() {
        return snackmasterStrings.TEXT[ZINGER_CUTOFF+4];
    }
}
