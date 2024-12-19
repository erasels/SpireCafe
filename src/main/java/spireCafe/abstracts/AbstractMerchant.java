package spireCafe.abstracts;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import spireCafe.screens.CafeMerchantScreen;

import java.util.ArrayList;

public abstract class AbstractMerchant extends AbstractCafeInteractable {
    public TextureRegion background;
    public ArrayList<AbstractArticle> articles = new ArrayList<>();
    public ArrayList<AbstractArticle> toAdd = new ArrayList<>();
    public ArrayList<AbstractArticle> toRemove = new ArrayList<>();
    public boolean wasShopRolled = false;

    public AbstractMerchant(float animationX, float animationY, float hb_w, float hb_h) {
        super(animationX, animationY, hb_w, hb_h);
    }

    public void initialize() {
        if (!wasShopRolled) {
            rollShop();
            wasShopRolled = true;
        }
    }

    //Override to populate the shop by adding AbstractArticle objects to the articles list
    protected abstract void rollShop();

    @Override
    public void onInteract() {
        BaseMod.openCustomScreen(CafeMerchantScreen.ScreenEnum.CAFE_MERCHANT_SCREEN, this);
    }

    //Override if you need special behavior when an article is bought
    public void onBuyArticle(AbstractArticle article) {
        toRemove.add(article);
    }

    //Called after the custom screen is close in case you need to take care of lingering effects or something
    public void onCloseShop() {}

    public void updateShop() {
        for (AbstractArticle article : toAdd) {
            articles.add(article);
        }
        toAdd.clear();
        for (AbstractArticle article : articles) {
            article.update();
        }
        for (AbstractArticle article : toRemove) {
            articles.remove(article);
        }
        toRemove.clear();
    }

    public void renderShop(SpriteBatch sb) {
        sb.setColor(Color.WHITE);
        sb.draw(background, 0f,0f, Settings.WIDTH, Settings.HEIGHT);
        for (AbstractArticle article : articles) {
            article.render(sb);
        }
    }
}
