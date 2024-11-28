package spireCafe.abstracts;

import com.megacrit.cardcrawl.shop.ShopScreen;

public abstract class AbstractMerchant extends AbstractCafeInteractable {
    public String name;
    public ShopScreen shopScreen;

    public AbstractMerchant(float animationX, float animationY, float hb_w, float hb_h) {
        super(animationX, animationY, hb_w, hb_h);
        shopScreen = createShopScreen();
    }

    public abstract ShopScreen createShopScreen();

    //TODO: Placement Logic
}
