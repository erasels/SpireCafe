package spireCafe.interactables.merchants.griddraft;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Affine2;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.helpers.Hitbox;

import spireCafe.Anniv7Mod;
import spireCafe.abstracts.AbstractArticle;
import spireCafe.abstracts.AbstractMerchant;
import spireCafe.util.TexLoader;

public class PurchasedGridCardArticle extends AbstractArticle {

        private static final Texture TEXTURE = TexLoader.getTexture(Anniv7Mod.makeMerchantPath("griddraft/blank.png"));
        private static float X_OFFSET = 604.0F * Settings.scale;
        private static float Y_OFFSET = 633.0F * Settings.scale;
        private static float X_PAD = 275.0F * Settings.scale;
        private static float Y_PAD = 300.0F * Settings.scale;
        private float rotate;


    public PurchasedGridCardArticle(AbstractMerchant merchant, int row, int column) {
        super("id" + row + column, merchant);
        updateGrid(row, column);
        this.itemTexture = new TextureRegion(TEXTURE);
        if (row % 2 == 0) {
            this.rotate = MathUtils.random(-5, 0);
        } else {
            this.rotate = MathUtils.random(0, 5);
        }
        this.hb = new Hitbox(192, 269);
    }

    private void updateGrid(int row, int column) {
        this.xPos = X_OFFSET + column * X_PAD;
        this.yPos = Y_OFFSET - row * Y_PAD;
    }

    @Override
    public void renderItem(SpriteBatch sb) {
        if (itemTexture != null) {
            Affine2 transform = new Affine2();
            transform.translate(xPos + (hb.width - scale * itemTexture.getRegionWidth())/2f,yPos + (hb.height - scale * itemTexture.getRegionHeight())/2f);
            transform.scale(scale,scale);
            transform.rotate(this.rotate);
            sb.draw(itemTexture, itemTexture.getRegionWidth(), itemTexture.getRegionHeight(), transform);
        }
    }

    @Override
    public boolean canBuy() {
        return false;
    }

    @Override
    public void onBuy() {
    }

    @Override
    public void onClick() {
    }

    @Override
    public int getBasePrice() {
        return 0;
    }

    @Override
    public void renderPrice(SpriteBatch sb) {
    }
    
}
