package spireCafe.interactables.merchants.peddler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.UIStrings;
import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;

public class NoRefunds extends AbstractArticle{
    private static final String ID = Anniv7Mod.makeID(NoRefunds.class.getSimpleName());
    private static final Texture SIGN = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("peddlermerchant/norefunds.png"));

    public NoRefunds(AbstractMerchant merchant, float x, float y) {
        super(ID, merchant, x, y, SIGN);
    }

    @Override
    public boolean canBuy() {
        return false;
    }

    @Override
    public void onBuy() {

    }

    @Override
    public int getBasePrice() {
        return 0;
    }

    @Override
    public void onClick() {
        ((PeddlerMerchant) merchant).noRefunds();
    }

    @Override
    public void render(SpriteBatch sb) {
        itemTexture = new TextureRegion(SIGN);
            sb.setColor(Color.WHITE);
            renderItem(sb);
    }
}
