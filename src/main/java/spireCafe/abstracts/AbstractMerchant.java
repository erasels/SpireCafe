package spireCafe.abstracts;

import basemod.BaseMod;
import com.badlogic.gdx.graphics.Texture;
import spireCafe.screens.CafeMerchantScreen;

import java.util.ArrayList;

public abstract class AbstractMerchant extends AbstractCafeInteractable {
    public String name;
    public Texture background;
    public ArrayList<AbstractArticle> articles = new ArrayList<>();

    public AbstractMerchant(float animationX, float animationY, float hb_w, float hb_h) {
        super(animationX, animationY, hb_w, hb_h);
    }

    @Override
    public void onInteract() {
        BaseMod.openCustomScreen(CafeMerchantScreen.ScreenEnum.CAFE_MERCHANT_SCREEN, this);
    }

    public void addArticle(AbstractArticle article) {
        articles.add(article);
        article.merchant = this;
    }

    public void buyArticle(AbstractArticle article) {
        articles.remove(article);
    }


    //TODO: Placement Logic
}
