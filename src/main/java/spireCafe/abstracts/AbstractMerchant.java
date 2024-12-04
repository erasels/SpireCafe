package spireCafe.abstracts;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import spireCafe.screens.CafeMerchantScreen;

import java.util.ArrayList;

public abstract class AbstractMerchant extends AbstractCafeInteractable {
    public String name;
    public TextureRegion background;
    public ArrayList<AbstractArticle> articles = new ArrayList<>();
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

    public void updateShop() {
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
        sb.draw(background, 0f,0f,0f,0f,background.getRegionWidth(),background.getRegionHeight(), Settings.scale, Settings.scale, 0f);
        for (AbstractArticle article : articles) {
            article.render(sb);
        }
    }


    //TODO: Placement Logic
}
