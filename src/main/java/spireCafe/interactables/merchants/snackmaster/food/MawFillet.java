package spireCafe.interactables.merchants.snackmaster.food;

import spireCafe.abstracts.AbstractMerchant;

import static spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant.ZINGER_CUTOFF;
import static spireCafe.interactables.merchants.snackmaster.SnackmasterMerchant.snackmasterStrings;

public class MawFillet extends AbstractFoodArticle{
    public MawFillet(AbstractMerchant merchant, int slot) {
        super("MawFillet", merchant, slot, "mawFillet");
    }

    @Override
    public void foodEffect() {
        //TODO: Give relic for 3 strength at combat start for the next 5 combats?
    }

    @Override
    public int getBasePrice() {
        return 120;
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
