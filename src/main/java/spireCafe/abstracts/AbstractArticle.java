package spireCafe.abstracts;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.helpers.Hitbox;
import spireCafe.abstracts.AbstractMerchant;

public abstract class AbstractArticle {

    public AbstractMerchant merchant;

    public Hitbox hb;
    public float xPos;
    public float yPos;

    public int price = -1; //negative prices are not shown
    public Texture priceIcon;

    public boolean canBuy() {
        return true;
    }

    public void onClick() {
        if (canBuy()) {
            onBuy();
            merchant.buyArticle(this);
        }
    }

    public void onBuy() {

    }

    public void update() {

    }

    public void render(SpriteBatch sb) {

    }
}
