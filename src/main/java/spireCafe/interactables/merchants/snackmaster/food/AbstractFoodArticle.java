package spireCafe.interactables.merchants.snackmaster.food;

import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;

public abstract class AbstractFoodArticle extends AbstractArticle {
    protected static final float BASE_X = 542f * Settings.xScale;
    protected static final float BASE_Y = 384f * Settings.yScale;
    protected static final float STEP_X = 305f * Settings.xScale;

    public AbstractFoodArticle(String id, AbstractMerchant merchant, int slot, String textureName) {
        super(id, merchant, BASE_X + (STEP_X * slot), BASE_Y, TexLoader.getTexture(Anniv7Mod.makeMerchantPath("snackmaster/food/"+textureName+".png")));
    }

    public boolean canBuy() {
        return AbstractDungeon.player.gold > getModifiedPrice();
    }

    @Override
    public void onBuy() {
        AbstractDungeon.player.loseGold(getModifiedPrice());
        foodEffect();
    }

    public abstract void foodEffect();
}
